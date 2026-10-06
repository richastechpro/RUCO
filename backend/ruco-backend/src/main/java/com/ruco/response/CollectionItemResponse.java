package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionItemResponse {

	private Long id;
	private String fboBranchName;
	private Float qty;
	private String date;
	private String description;
	private String collectedStatus;
	
}
