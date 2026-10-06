package com.ruco.service.audit;

import java.util.Map;

import javax.persistence.PostUpdate;
import javax.persistence.PreUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditEntityListener {

	private static AuditService auditService;

	@Autowired
	public void init(AuditService service) {
		auditService = service;
	}

	private static final ThreadLocal<Map<String, Object>> OLD_STATE = new ThreadLocal<>();

	@PreUpdate
	public void beforeUpdate(Object entity) {
		OLD_STATE.set(AuditUtils.mapObject(entity));
	}

	@PostUpdate
	public void afterUpdate(Object entity) {
		Map<String, Object> oldMap = OLD_STATE.get();
		Map<String, Object> newMap = AuditUtils.mapObject(entity);

		Map<String, Object> diff = AuditUtils.diffMaps(oldMap, newMap);
		auditService.logAction(entity, AuditAction.UPDATE, oldMap, newMap, diff);

		OLD_STATE.remove(); // cleanup
	}

}