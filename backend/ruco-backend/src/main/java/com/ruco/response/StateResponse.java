package com.ruco.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateResponse {

	private Long stateId;
	private String stateName;
	private Set<CityResponse> cities;
	
}
