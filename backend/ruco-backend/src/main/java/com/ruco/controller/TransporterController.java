package com.ruco.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruco.model.Transporter;
import com.ruco.request.SearchRequest;
import com.ruco.service.TransporterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transporter")
public class TransporterController {

	private final TransporterService transporterService;

	@PostMapping("get")
	public ResponseEntity<?> get(@RequestBody SearchRequest request) {
		Page<?> items = transporterService.get(request);
		return ResponseEntity.ok().body(items);
	}

	@PostMapping
	private ResponseEntity<?> create(@RequestBody Transporter transporter) {
		transporterService.create(transporter);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping
	private ResponseEntity<?> update(@RequestBody Transporter transporter) {
		transporterService.update(transporter);
		return ResponseEntity.ok(HttpStatus.OK);
	}


	@GetMapping(value = "{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		Transporter transporter = transporterService.get(id);
		return ResponseEntity.ok().body(transporter);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		transporterService.delete(id);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "/valid/{collectionId}")
	public ResponseEntity<?> getValid(@PathVariable Long collectionId) {
		List<Transporter> items = transporterService.getValid(collectionId);
		return ResponseEntity.ok().body(items);
	}

}
