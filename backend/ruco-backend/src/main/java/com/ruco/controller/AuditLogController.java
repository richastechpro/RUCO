package com.ruco.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruco.request.AuditRequest;
import com.ruco.service.audit.AuditService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/audit")
public class AuditLogController {

	private final AuditService auditService;
	private final com.ruco.service.audit.hibernate.AuditPDFService auditService2;

	@PostMapping
	public void downloadAuditReport(@RequestBody AuditRequest request, HttpServletResponse response) {
		try {
			File csvFile = auditService.generateAuditCsv(1l);

			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + csvFile.getName() + "\"");
			response.setContentLength((int) csvFile.length());

			try (FileInputStream fis = new FileInputStream(csvFile); OutputStream os = response.getOutputStream()) {
//				fis.transferTo(os);
				os.flush();
			}

			// Optionally delete after sending
			Files.deleteIfExists(csvFile.toPath());

		} catch (Exception e) {
			// Safe to send error here — response not yet committed
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating audit CSV report");
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	@PostMapping("log")
	public void get(@RequestBody AuditRequest request, HttpServletResponse response) {
		try {
			File csvFile = auditService2.writeAuditPdf(1l);

			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=audit_report" + ".pdf");
			response.setContentLength((int) csvFile.length());

			try (FileInputStream fis = new FileInputStream(csvFile); OutputStream os = response.getOutputStream()) {
//				fis.transferTo(os);
				os.flush();
			}

			// Optionally delete after sending
			Files.deleteIfExists(csvFile.toPath());

		} catch (Exception e) {
			// Safe to send error here — response not yet committed
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating audit CSV report");
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
}
