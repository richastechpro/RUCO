package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAllresponse {

	private String branchName;
	private String warehouse;
	private String state;
	private String city;
	private Float qty;

}
