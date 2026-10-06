package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

	private Long id;
	private String loginId;
	private String password;
	private String username;
	private String email;
	private Long mobileNo;
	private String userType;
	private Long branchId;

}
