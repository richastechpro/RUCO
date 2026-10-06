package com.ruco.service.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

	private Long id;
	private Long userId;
	private String userType;
	private String tableName;
	private AuditAction action;
	private String details;
	private String timestamp;
	
}
