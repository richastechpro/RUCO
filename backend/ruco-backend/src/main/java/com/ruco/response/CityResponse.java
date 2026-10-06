package com.ruco.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityResponse {

	private Long cityId;
	private String cityName;
	private Set<AreaResponse> areas;

}
