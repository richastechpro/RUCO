package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FBOBranchLoginRequest {

	private Long id;
	private String email;
	private String password;
	private String username;
	private Long mobileNo;
	private String type;
	private Long stateId;
	private Long cityId;
	private Long fboBranchId;
	
}
