package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {

	private Long id;
	private String companyName;
	private String companyAddress;
	private StateResponse state;
	private CityResponse city;
	private AreaResponse area;
	private String contactPerson;
	private Long mobileNo;
	private String emailId;
	private String panNo;
	private String gstNo;

}
