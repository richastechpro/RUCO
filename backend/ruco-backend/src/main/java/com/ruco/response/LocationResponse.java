package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse {

	private String stateName;
	private String cityName;
	private String areaName;

	private Long stateId;
	private Long cityId;
	private Long areaId;

}
