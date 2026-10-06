package com.ruco.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

	private Float totalQty;
	private Float totalAmount;
	private List<InventoryItemResponse> items;

}
