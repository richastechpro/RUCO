package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryOutResponse {

	private Long id;
	private String addedBy;
	private String branchName;
	private String customerName;
	private Float qty;
	private String comment;
	private String vehicleNo;
	private String transporter;

}
