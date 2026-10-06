package com.ruco.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruco.response.DashboardResponse;
import com.ruco.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/dashboard")
public class DashboardController {

	private final DashboardService dashboardService;

	@GetMapping
	public ResponseEntity<?> get() {
		List<DashboardResponse> data = dashboardService.getData();
		return ResponseEntity.ok().body(data);
	}

}
