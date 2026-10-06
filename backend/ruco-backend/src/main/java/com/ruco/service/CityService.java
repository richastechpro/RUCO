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
public class CityService {

	private final StateRepository stateRepository;
	private final CityRepository cityRepository;
	private final AreaRepository areaRepository;

	public void create(LocationRequest request) {
		Long stateId = request.getStateId();
		Optional<State> stateOptional = stateRepository.findById(stateId);
		if (stateOptional.isPresent()) {
			State state = stateOptional.get();
			City city = new City();
			city.setCityName(request.getCityName());
			city.setState(state);
			cityRepository.save(city);
		}
	}

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		String searchTxt = request.getSearchTxt();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "cityName"));
		Page<City> pages = cityRepository.findAll(SearchBuilder.cityHasSearchText(searchTxt), pageable);
		return pages.map(this::mapData);
	}

	private LocationResponse mapData(City city) {
		State state = city.getState();
		LocationResponse response = new LocationResponse();

		response.setCityId(city.getId());
		response.setCityName(city.getCityName());

		if (state != null) {
			response.setStateId(state.getId());
			response.setStateName(state.getStateName());
		}

		return response;
	}

	public City get(Long id) {
		return cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area not found"));
	}

	public void update(LocationRequest request) {
		Long cityId = request.getCityId();
		Optional<City> cityOptional = cityRepository.findById(cityId);
		if (cityOptional.isPresent()) {
			City city = cityOptional.get();
			String cityName = request.getCityName();
			city.setCityName(cityName);
			cityRepository.save(city);
		}
	}

	public void delete(Long id) {
		Optional<City> cityOptional = cityRepository.findById(id);
		if (cityOptional.isPresent()) {
			City city = cityOptional.get();
			Set<Area> areas = city.getAreas();
			areas.forEach(a -> {
				a.setIsDelete(true);
				areaRepository.save(a);
			});
			city.setIsDelete(true);
			cityRepository.save(city);
		}
	}

}
