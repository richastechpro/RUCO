package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchRequest {

	private Long id;
	private String branchName;
	private String branchAddress;
	private Long stateId;
	private Long cityId;
	private Long areaId;
	private String gstNo;
	private String panNo;
	private String warehouseName;
	private String warehouseAddress;
	
}
