package com.ruco.response;

import java.util.List;

import com.ruco.model.InventoryOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionResponse {

	private Long id;
	private String collectionName;
	private String type;
	private String requestDate;
	private Float qty;
	private Float collectedQty;
	private Boolean isAssigned;
	private Integer collected;
	private Long requestId;
	private Boolean inventoryIn = Boolean.FALSE;
	private InventoryOut inventoryOut;
	private String branchName;
	private Long branchId;
	private String warehouse;
	private List<CollectionItemDetail> details;
}
