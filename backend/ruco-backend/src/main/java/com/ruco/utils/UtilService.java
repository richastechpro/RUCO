package com.ruco.utils;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class UtilService {

	public static Map<String, String> getTableMapping() {
		Map<String, String> tableMap = new HashMap<>();

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

		scanner.findCandidateComponents("com.ruco.model").forEach(beanDefinition -> {
			try {
				Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
				String className = clazz.getSimpleName();
				String tableName;

				if (clazz.isAnnotationPresent(Table.class)) {
					Table table = clazz.getAnnotation(Table.class);
					tableName = (table.name() != null && !table.name().isEmpty()) ? table.name()
							: className.toLowerCase();
				} else {
					tableName = className.toLowerCase();
				}

				tableMap.put(className, tableName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});

		return tableMap;
	}

}
