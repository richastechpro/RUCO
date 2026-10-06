package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionItemDetail {

	private Float assignedQty;
	private Float collectedQty;
	private Boolean inventoryIn;
	private String fboBranchName;

}