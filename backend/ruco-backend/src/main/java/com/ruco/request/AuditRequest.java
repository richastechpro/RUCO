package com.ruco.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditRequest {

	private String tableName;
	private String action;
	private String userType;
	private Long userId;

}
