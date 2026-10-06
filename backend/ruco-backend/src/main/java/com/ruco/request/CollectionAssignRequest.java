package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionAssignRequest {

	private Long collectionId;
    private Long user1;
    private Long user2;
    private Long transporter;

}
