package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionCollectRequest {

    private Long collectionId;
    private Long requestId;
    private String collectionType;
    private Float collectedQty;
    private String collectedOn;
    private String invoiceNo;
    private String descr;

}
