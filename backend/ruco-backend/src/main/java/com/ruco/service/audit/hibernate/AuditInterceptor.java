package com.ruco.service.audit.hibernate;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruco.service.audit.AuditAction;
import com.ruco.service.audit.AuditService;
import com.ruco.service.audit.AuditUtils;

@Component
public class AuditInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;

	private static AuditService auditService;

	@Autowired
	public void init(AuditService auditService) {
		AuditInterceptor.auditService = auditService;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {

		// Skip auditing the AuditLog entity itself to avoid recursion
		if (entity.getClass().getSimpleName().equals("AuditLog")) {
			return false;
		}

		if (previousState == null) {
			return false;
		}

		Long primaryKey = (Long) id;
		Map<String, Object> diff = new LinkedHashMap<>();
		diff.put("id", primaryKey);

		AuditAction action = AuditAction.UPDATE;

		for (int i = 0; i < propertyNames.length; i++) {
			Object oldVal = previousState[i];
			Object newVal = currentState[i];

			if (propertyNames[i].equals("isDelete") && oldVal.equals(false) && newVal.equals(true)) {
				action = AuditAction.DELETE;
			}

			if ((oldVal == null && newVal != null) || (oldVal != null && !oldVal.equals(newVal))) {
				Map<String, Object> fieldDiff = new LinkedHashMap<>();
				fieldDiff.put("old", oldVal);
				fieldDiff.put("new", newVal);
				diff.put(propertyNames[i], fieldDiff);
			}
		}

		if (!diff.isEmpty()) {
			String className = entity.getClass().getSimpleName();
			auditService.logAction(action, className, diff);
		}

		return false; // return true only if you actually modified currentState
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

		if (entity.getClass().getSimpleName().equals("AuditLog")) {
			return false;
		}

		String className = entity.getClass().getSimpleName();
		Map<String, Object> map = AuditUtils.mapObject(entity);
		auditService.logAction(AuditAction.CREATE, className, map);
		return false;
	}

//	@SuppressWarnings({ "rawtypes" })
//	@Override
//	public void postFlush(Iterator entities) {
//		Iterator<?> iterator = entities;
//		iterator.forEachRemaining(extracted());
//		super.postFlush(entities);
//	}
//
//	@SuppressWarnings("rawtypes")
//	private Consumer extracted() {
//		return e -> {
//			Class<? extends Object> clazz = e.getClass();
//			if (clazz.getSimpleName().equals("AuditLog")) {
//				return;
//			}
//			System.out.println(clazz);
//		};
//	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

		if (entity.getClass().getSimpleName().equals("AuditLog")) {
			return;
		}

		String className = entity.getClass().getSimpleName();
		Map<String, Object> map = AuditUtils.mapObject(entity);
		auditService.logAction(AuditAction.DELETE, className, map);
	}
}