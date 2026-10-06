package com.ruco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruco.request.ReportRequest;
import com.ruco.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/report")
public class ReportController {

	private final ReportService reportService;

	@PostMapping
	public ResponseEntity<?> getReports(@RequestBody ReportRequest request) {
		return reportService.getReports(request);
	}

}
