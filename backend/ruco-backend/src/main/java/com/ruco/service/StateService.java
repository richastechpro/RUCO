package com.ruco.service;

import java.util.List;
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
public class StateService {

	private final StateRepository stateRepository;
	private final CityRepository cityRepository;
	private final AreaRepository areaRepository;

	public List<State> getValid() {
		List<State> states = stateRepository.findAllByIsDeleteFalseOrderByStateNameAsc();
		return states;
	}

	public void create(LocationRequest request) {
		String stateName = request.getStateName();
		State state = new State();
		state.setStateName(stateName);
		stateRepository.save(state);
	}

	@Transactional(readOnly = true)
	public Page<?> get(SearchRequest request) {
		Integer limit = request.getLimit();
		Integer offset = request.getOffset();
		String searchTxt = request.getSearchTxt();
		Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "stateName"));
		Page<State> pages = stateRepository.findAll(SearchBuilder.stateHasSearchText(searchTxt), pageable);
		return pages.map(this::mapData);
	}

	private LocationResponse mapData(State state) {
		LocationResponse response = new LocationResponse();
		response.setStateName(state.getStateName());
		response.setStateId(state.getId());
		return response;
	}

	public State get(Long id) {
		return stateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Area not found"));
	}

	public void update(LocationRequest request) {
		Long stateId = request.getStateId();
		Optional<State> stateOptional = stateRepository.findById(stateId);
		if (stateOptional.isPresent()) {
			State state = stateOptional.get();
			state.setStateName(request.getStateName());
			stateRepository.save(state);
		}
	}

	public void delete(Long id) {
		Optional<State> stateOptional = stateRepository.findById(id);
		if (stateOptional.isPresent()) {
			State state = stateOptional.get();
			Set<City> cities = state.getCities();
			cities.forEach(c -> {
				Set<Area> areas = c.getAreas();
				areas.forEach(a -> {
					a.setIsDelete(true);
					areaRepository.save(a);
				});
				c.setIsDelete(true);
				cityRepository.save(c);
			});
			state.setIsDelete(true);
			stateRepository.save(state);
		}
	}

}
