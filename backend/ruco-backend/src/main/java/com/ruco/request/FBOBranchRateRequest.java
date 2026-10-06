package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FBOBranchRateRequest {

	private Long fboBranchId;
	private String fboBranchName;
	private Float rate;

}
