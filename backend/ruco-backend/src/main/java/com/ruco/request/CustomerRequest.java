package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

	private Long id;
	private String customerName;
	private String customerAddress;
	private String customerType;
	private String email;
	private Long mobileNo;
	
}
