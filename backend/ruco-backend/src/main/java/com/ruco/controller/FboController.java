package com.ruco.controller;

import java.util.Arrays;
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

import com.ruco.model.FBO;
import com.ruco.request.FBORequest;
import com.ruco.request.SearchRequest;
import com.ruco.response.FBOResponse;
import com.ruco.response.FboBranchLoginResponse;
import com.ruco.service.FboService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/fbo")
public class FboController {

	private final FboService fboService;

	@PostMapping("get")
	public ResponseEntity<?> get(@RequestBody SearchRequest request) {
		Page<?> items = fboService.get(request);
		return ResponseEntity.ok().body(items);
	}

	@PostMapping
	private ResponseEntity<?> create(@RequestBody FBORequest request) {
		fboService.create(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping
	private ResponseEntity<?> update(@RequestBody FBO branch) {
		fboService.update(branch);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		FBOResponse branch = fboService.get(id);
		return ResponseEntity.ok().body(branch);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		fboService.delete(id);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping("logins/{fboId}")
	public ResponseEntity<?> getFboLogins(@PathVariable Long fboId) {
		List<FboBranchLoginResponse> response = fboService.getFboLogins(fboId);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("fbo-name/{fboId}")
	public ResponseEntity<?> getFboName(@PathVariable Long fboId) {
		String response = fboService.getFboName(fboId);
		return ResponseEntity.ok().body(Arrays.asList(response));
	}

}
