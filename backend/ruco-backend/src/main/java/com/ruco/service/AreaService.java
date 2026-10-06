package com.ruco.service;

import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruco.constant.SearchBuilder;
import com.ruco.model.Area;
import com.ruco.model.City;
import com.ruco.model.State;
import com.ruco.repo.AreaRepository;
import com.ruco.repo.CityRepository;
import com.ruco.repo.StateRepository;
import com.ruco.request.LocationRequest;
import com.ruco.request.SearchRequest;
import com.ruco.response.LocationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AreaService {

	private final StateRepository stateRepository;
	private final CityRepository cityRepository;
	private final AreaRepository areaRepository;

	public void create(LocationRequest request) {
		Long stateId = request.getStateId();
		Optional<State> stateOptional = stateRepository.findById(stateId);
		if (stateOptional.isPresent()) {
			State state = stateOptional.get();
			Long cityId = request.getCityId();
			Optional<City> cityOptional = cityRepository.findById(cityId);
			if (cityOptional.isPresent()) {
				City city = cityOptional.get();
				city.setState(state);
				Set<Area> areas = city.getAreas();
				Area area = new Area();
				String areaName = request.getAreaName();
				area.setAreaName(areaName);
				area = areaRepository.save(area);
				areas.add(area);
				city.setAreas(areas);
				cityRepository.save(city);
			}
		}
	}

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		String searchTxt = request.getSearchTxt();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "areaName"));
		Page<Area> pages = areaRepository.findAll(SearchBuilder.areaHasSearchText(searchTxt), pageable);
		return pages.map(this::mapData);
	}

	private LocationResponse mapData(Area area) {
		City city = area.getCity();
		State state = city != null ? city.getState() : null;

		LocationResponse response = new LocationResponse();
		response.setAreaId(area.getId());
		response.setAreaName(area.getAreaName());

		if (city != null) {
			response.setCityId(city.getId());
			response.setCityName(city.getCityName());
		}

		if (state != null) {
			response.setStateId(state.getId());
			response.setStateName(state.getStateName());
		}

		return response;
	}

	public Area get(Long id) {
		return areaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area not found"));
	}

	public void update(LocationRequest request) {
		Long areaId = request.getAreaId();
		Optional<Area> areaOptional = areaRepository.findById(areaId);
		if (areaOptional.isPresent()) {
			Area area = areaOptional.get();
			area.setAreaName(request.getAreaName());
			areaRepository.save(area);
		}
	}

	public void delete(Long id) {
		Optional<Area> areaOptional = areaRepository.findById(id);
		if (areaOptional.isPresent()) {
			Area area = areaOptional.get();
			area.setIsDelete(true);
			areaRepository.save(area);
		}
	}

}
