package com.ruco.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkCollectionResponse {

	private String collectionName;
	private List<CollectionItemResponse> items;
	
}
