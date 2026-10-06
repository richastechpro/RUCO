package com.ruco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruco.request.InventoryAllRequest;
import com.ruco.response.InventoryResponse;
import com.ruco.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/inventory")
public class InventoryController {

	private final InventoryService inventoryService;

	@PostMapping
	public ResponseEntity<?> getInventoryAll(@RequestBody InventoryAllRequest request) {
		InventoryResponse response = inventoryService.getInventoryAll(request);
		return ResponseEntity.ok().body(response);
	}

}
