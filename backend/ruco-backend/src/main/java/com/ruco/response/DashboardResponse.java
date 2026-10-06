package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

	private String label;
	private String route;
	private Object count;
	private String imgUrl;
	private String gradient;

}
