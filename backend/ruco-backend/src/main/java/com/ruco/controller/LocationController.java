package com.ruco.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruco.response.StateResponse;
import com.ruco.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/location")
public class LocationController {

	private final LocationService locationService;

	@GetMapping
	public ResponseEntity<?> get() {
		List<StateResponse> states = locationService.getLocations();
		return ResponseEntity.ok().body(states);
	}

}
