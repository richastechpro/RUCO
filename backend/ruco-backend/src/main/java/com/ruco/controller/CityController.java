package com.ruco.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruco.request.LocationRequest;
import com.ruco.request.SearchRequest;
import com.ruco.service.CityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/city")
public class CityController {

	private final CityService cityService;

	@PostMapping("get")
	public ResponseEntity<?> get(@RequestBody SearchRequest request) {
		Page<?> items = cityService.get(request);
		return ResponseEntity.ok().body(items);
	}

	@PostMapping
	private ResponseEntity<?> create(@RequestBody LocationRequest request) {
		cityService.create(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping
	private ResponseEntity<?> update(@RequestBody LocationRequest request) {
		cityService.update(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	private ResponseEntity<?> delete(@PathVariable Long id) {
		cityService.delete(id);
		return ResponseEntity.ok(HttpStatus.OK);
	}

}
