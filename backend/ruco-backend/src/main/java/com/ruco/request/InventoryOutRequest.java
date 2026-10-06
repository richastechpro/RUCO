package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryOutRequest {

	private Long branchId;
	private Long customerId;
	private Float qty;
	private String comment;
	private String vehicleNo;
	private String transporter;

}
