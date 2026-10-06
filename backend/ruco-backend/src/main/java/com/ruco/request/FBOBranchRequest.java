package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FBOBranchRequest {

	private Long id;
	private String fboBranchName;
	private String fboBranchAddress;
	private String representative;
	private Long stateId;
	private Long cityId;
	private Long areaId;
	private Long branchId;
	private String email;
	private Long mobileNo;
	private String gstNo;
	private String fssaiNo;
	private Float rate;
	
}
