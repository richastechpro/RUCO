package com.ruco.service.audit;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AuditUtils {

	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	/** Converts any entity to a Map<String, Object> recursively **/
	public static Map<String, Object> mapObject(Object obj) {
		Map<String, Object> fieldMap = new LinkedHashMap<>();

		if (obj == null)
			return fieldMap;

		Class<?> clazz = obj.getClass();

		while (clazz != null && clazz != Object.class) {
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				try {
					String name = field.getName();
					Object value = field.get(obj);

					if (value == null) {
						fieldMap.put(name, null);
						continue;
					}

					AuditCollection annotation = field.getAnnotation(AuditCollection.class);

					// Handle collections
					if (value instanceof Collection<?> && (annotation == null || !annotation.ignore())) {
						List<Object> list = new ArrayList<>();
						for (Object element : (Collection<?>) value) {
							if (isPojo(element.getClass())) {
								list.add(mapObject(element));
							} else {
								list.add(element);
							}
						}
						fieldMap.put(name, list);
					}
					// Handle nested objects
					else if (isPojo(field.getType())) {
						if (annotation == null || !annotation.ignore()) {
							fieldMap.put(name, mapObject(value));
						}
					}
					// Handle LocalDateTime
					else if (field.getType().equals(LocalDateTime.class)) {
						LocalDateTime dateTime = (LocalDateTime) value;
						fieldMap.put(name, DF.format(dateTime));
					}
					// Handle primitives & others
					else {
						fieldMap.put(name, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			clazz = clazz.getSuperclass();
		}

		return fieldMap;
	}

	/** Compares two entity maps and returns a diff {field: {old, new}} **/
	public static Map<String, Object> diffMaps(Map<String, Object> oldMap, Map<String, Object> newMap) {
		Map<String, Object> diff = new LinkedHashMap<>();

		if (newMap == null)
			return diff;

		for (String key : newMap.keySet()) {
			Object oldVal = oldMap != null ? oldMap.get(key) : null;
			Object newVal = newMap.get(key);

			// Compare deeply (but shallowly for collections)
			if (!Objects.equals(oldVal, newVal)) {
				Map<String, Object> change = new LinkedHashMap<>();
				change.put("old", oldVal);
				change.put("new", newVal);
				diff.put(key, change);
			}
		}
		return diff;
	}

	/** Detects if a class is a user-defined POJO **/
	public static boolean isPojo(Class<?> clazz) {
		if (clazz.isPrimitive())
			return false;
		if (clazz.getName().startsWith("java."))
			return false;
		if (clazz.getName().startsWith("javax."))
			return false;
		if (Collection.class.isAssignableFrom(clazz))
			return false;
		if (Map.class.isAssignableFrom(clazz))
			return false;
		if (clazz.isEnum())
			return false;
		return true;
	}
}