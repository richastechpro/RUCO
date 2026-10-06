package com.ruco.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruco.model.Area;
import com.ruco.model.City;
import com.ruco.model.State;
import com.ruco.repo.StateRepository;
import com.ruco.response.AreaResponse;
import com.ruco.response.CityResponse;
import com.ruco.response.StateResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

	private final StateRepository stateRepository;

	@Transactional
	public List<StateResponse> getLocations() {
		List<State> states = stateRepository.findAllByIsDeleteFalseOrderByStateNameAsc();
		List<StateResponse> stateData = states.stream().map(state -> {
			StateResponse stateResponse = new StateResponse();
			stateResponse.setStateId(state.getId());
			stateResponse.setStateName(state.getStateName());
			Set<City> cities = state.getCities();
			Set<CityResponse> cityData = cities.stream().map(city -> {
				CityResponse cityResponse = new CityResponse();
				cityResponse.setCityId(city.getId());
				cityResponse.setCityName(city.getCityName());
				Set<Area> areas = city.getAreas();
				Set<AreaResponse> areaData = areas.stream().map(area -> {
					AreaResponse areaResponse = new AreaResponse();
					areaResponse.setAreaId(area.getId());
					areaResponse.setAreaName(area.getAreaName());
					return areaResponse;
				}).collect(Collectors.toSet());
				cityResponse.setAreas(areaData);
				return cityResponse;
			}).collect(Collectors.toSet());
			stateResponse.setCities(cityData);
			return stateResponse;
		}).collect(Collectors.toList());
		return stateData;
	}

}
