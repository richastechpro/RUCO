package com.ruco.service.audit;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.ruco.config.CustomUserDetails;
import com.ruco.service.UserService;
import com.ruco.utils.UtilService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

	private final AuditLogRepository auditLogRepository;
	private static final Map<String, String> tableMapping = UtilService.getTableMapping();
	private final ObjectMapper mapper;
	private final UserService userService;
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Transactional
	public void logAction(Object entity, AuditAction action, Map<String, Object> oldState, Map<String, Object> newState,
			Map<String, Object> diff) {

		CustomUserDetails user = userService.getUser();
		String tableName = tableMapping.get(entity.getClass().getSimpleName());

		AuditLog log = new AuditLog();
		log.setUserId(user.getId());
		log.setUserType(user.getUserType());
		log.setTableName(tableName);
		log.setAction(action);
//		log.setTableId((Long) newState.get("id"));

		try {
			Map<String, Object> details = new LinkedHashMap<>();
			details.put("before", oldState);
			details.put("after", newState);
			details.put("diff", diff);

			log.setDetails(mapper.writeValueAsString(details));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		auditLogRepository.save(log);
	}

	@Transactional
	public void logBusinessAction(String event, String referenceId, String username) {
		AuditLog log = new AuditLog();
		auditLogRepository.save(log);
	}

	@Transactional(readOnly = true)
	public File generateAuditCsv(Long userId) throws Exception {
		File tempFile = File.createTempFile("audit_report_user_" + userId + "_", ".csv");
		tempFile.deleteOnExit();

		try (Writer writer = new FileWriter(tempFile);
				Stream<AuditLog> stream = auditLogRepository.streamByUserId(userId)) {
			StatefulBeanToCsv<AuditLogResponse> beanToCsv = new StatefulBeanToCsvBuilder<AuditLogResponse>(writer)
					.withSeparator(CSVWriter.DEFAULT_SEPARATOR).withQuotechar('"').build();

			stream.map(auditLog -> {
				return new AuditLogResponse(auditLog.getId(), auditLog.getUserId(), auditLog.getUserType(),
						auditLog.getTableName(), auditLog.getAction(), formatDetailsForCsv(auditLog.getDetails()),
						auditLog.getTimestamp().format(df));
			}).forEach(dto -> {
				try {
					beanToCsv.write(dto);
				} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
					throw new RuntimeException("Error writing CSV row", e);
				}
			});

			writer.flush();
		}

		return tempFile;
	}

	private String formatDetailsForCsv(String detailsJson) {
		if (detailsJson == null || detailsJson.isEmpty())
			return "";

		try {
			JsonNode root = mapper.readTree(detailsJson);
			StringBuilder sb = new StringBuilder();

			root.fields().forEachRemaining(entry -> {
				String fieldName = entry.getKey();
				JsonNode valueNode = entry.getValue();

				if (fieldName.equals("id")) {
					return;
				}

				String oldVal = valueNode.has("old") ? valueNode.get("old").asText("") : "";
				String newVal = valueNode.has("new") ? valueNode.get("new").asText("") : "";

				sb.append(
						// color fieldName in BOLD BLACK
						fieldName).append(" : {").append(
								// color oldVal in BOLD RED
								oldVal)
						.append(" -->> ").append(
								// color newVal in BOLD GREEN
								newVal)
						.append("},  ");
			});

			return sb.toString().trim();
		} catch (Exception e) {
			return detailsJson;
		}
	}

	public void logAction(AuditAction action, String className, Map<String, Object> diff) {
		CustomUserDetails user = userService.getUser();
		AuditLog log = new AuditLog();
		String tableName = tableMapping.get(className);
		log.setUserId(user.getId());
		log.setUserType(user.getUserType());
		log.setTableName(tableName);
		log.setAction(action);
		try {
			log.setDetails(mapper.writeValueAsString(diff));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		auditLogRepository.save(log);

	}
}