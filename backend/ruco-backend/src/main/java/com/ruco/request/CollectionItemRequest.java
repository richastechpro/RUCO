package com.ruco.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionItemRequest {

	private Long id;
	private Long fboBranchId;
	private String requestDate;
	private Float qty;
	private String description;
	private Long addedBy;
	private Boolean isAssigned = Boolean.FALSE;
	private Long assignedBy;
	private LocalDateTime pickupboyAssignedOn;
	private String collectedStatus;
	private Float collectedQty;
	private Long collectedBy;
	private String collectedOn;
	private String latitude;
	private String longitude;
	private Long collectionId;

}
