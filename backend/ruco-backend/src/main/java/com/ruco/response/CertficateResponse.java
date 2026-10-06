package com.ruco.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertficateResponse {

	private String fbo_name;
	private String certificate_no;
	private String collected_on;
	private String invoice_no;
	private Float collected_qty;

}
