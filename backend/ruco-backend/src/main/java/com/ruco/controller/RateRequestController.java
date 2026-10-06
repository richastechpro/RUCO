package com.ruco.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruco.request.SearchRequest;
import com.ruco.service.RateRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/rate-request")
public class RateRequestController {

	private final RateRequestService rateRequestService;

	@PostMapping("get")
	public ResponseEntity<?> get(@RequestBody SearchRequest request) {
		Page<?> items = rateRequestService.get(request);
		return ResponseEntity.ok().body(items);
	}

	@GetMapping("{id}/approve")
	public ResponseEntity<?> approve(@PathVariable Long id) {
		rateRequestService.approve(id);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}
	
	@GetMapping("{id}/reject")
	public ResponseEntity<?> reject(@PathVariable Long id) {
		rateRequestService.reject(id);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}
}
