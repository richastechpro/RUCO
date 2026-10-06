package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateRequestResponse {

	private Long id;
	private Float oldPrice;
	private Float newPrice;
	private Long fboBranchId;
	private String fboBranchName;
	private Long userId;
	private String username;
	private String status;

}
