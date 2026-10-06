package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FboBranchLoginResponse {

	private Long id;
	private String password;
	private String username;
	private String email;
	private Long mobileNo;
	private String type; 
	private StateResponse state;
	private CityResponse city;
	private FboBranchResponse fboBranch;
	
}
