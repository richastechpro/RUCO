package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WareHouseRequest {

	private String warehouseName;
	private String address;
	private Long branchId;
	
}
