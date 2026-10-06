package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FBORequest {

	private String fboName;
	private String bankName;
	private String accountType;
	private String accountNo;
	private String accountHolderName;
	private String ifscCode;
	private Long mobileNo;
	
}
