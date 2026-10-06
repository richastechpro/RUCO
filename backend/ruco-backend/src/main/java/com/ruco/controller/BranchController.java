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

import com.ruco.request.BranchRequest;
import com.ruco.request.SearchRequest;
import com.ruco.response.BranchResponse;
import com.ruco.service.BranchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/branch")
public class BranchController {

	private final BranchService branchService;

	@PostMapping("get")
	public ResponseEntity<?> get(@RequestBody SearchRequest request) {
		Page<?> items = branchService.get(request);
		return ResponseEntity.ok().body(items);
	}

	@PostMapping
	private ResponseEntity<?> create(@RequestBody BranchRequest request) {
		branchService.create(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping
	private ResponseEntity<?> update(@RequestBody BranchRequest request) {
		branchService.update(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		BranchResponse branch = branchService.get(id);
		return ResponseEntity.ok().body(branch);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		branchService.delete(id);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "valid")
	public ResponseEntity<?> getValid() {
		List<BranchResponse> branches = branchService.getValid();
		return ResponseEntity.ok().body(branches);
	}

	@GetMapping(value = "valid/{stateId}")
	public ResponseEntity<?> getValidByStateId(@PathVariable Long stateId) {
		List<BranchResponse> branches = branchService.getValidByStateId(stateId);
		return ResponseEntity.ok().body(branches);
	}

	@GetMapping(value = "qty/{id}")
	public ResponseEntity<?> getQty(@PathVariable Long id) {
		Float qty = branchService.getQty(id);
		return ResponseEntity.ok().body(qty);
	}

}
