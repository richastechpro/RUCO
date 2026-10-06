package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

	private Long id;
	private String loginId;
	private String username;
	private String email;
	private Long mobileNo;
	private String userType;
	private Long branchId;
	private String branchName;

}
