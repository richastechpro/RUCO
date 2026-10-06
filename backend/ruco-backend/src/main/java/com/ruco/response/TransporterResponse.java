package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransporterResponse {

	private Long id;
	private String contactPerson;
	private String transporterName;
	private String transporterAddress;
	private Long stateId;
	private Long cityId;
	private Long areaId;
	private Long mobileNo;
	private String vehicleNo;
	private String licenseNo;
	private String gstNo;
	private String panNo;
	private String contractDate;
	private Float ratePerKg;
	private Float collectedQty = 0f;
	private Long branchId;
	private String branchName;

}
