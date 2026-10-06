package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {

	private Long id;
	private String companyName;
	private String companyAddress;
	private Long stateId;
	private Long cityId;
	private Long areaId;
	private String contactPerson;
	private Long mobileNo;
	private String emailId;
	private String panNo;
	private String gstNo;
	
}
