package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse {

	private Long id;
	private String branchName;
	private String branchAddress;
	private StateResponse state;
	private CityResponse city;
	private AreaResponse area;
	private String gstNo;
	private String panNo;
	private String warehouseName;
	private String warehouseAddress;
	
}
