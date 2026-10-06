package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {

	private Long stateId;
	private Long cityId;
	private Long areaId;
	private String stateName;
	private String cityName;
	private String areaName;
	
}
