package com.ruco.response;

import com.ruco.model.Rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FboBranchResponse {

	private Long id;
	private String fboBranchName;
	private String fboBranchAddress;
	private String representative;
	private Long stateId;
	private Long cityId;
	private Long areaId;
	private Long branchId;
	private Double latitude;
	private Double longitude;
	private String email;
	private Long mobileNo;
	private String gstNo;
	private String fssaiNo;
	private String stateName;
	private String cityName;
	private String areaName;
	private String branchName;
	private StateResponse state;
	private CityResponse city;
	private AreaResponse area;
	private BranchResponse branch;
	private Rate rate;

}
