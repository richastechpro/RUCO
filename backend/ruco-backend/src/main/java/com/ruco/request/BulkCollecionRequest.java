package com.ruco.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkCollecionRequest {

	private String collectionName;
	private Long branchId;
	private List<CollectionItemRequest> items;

}
