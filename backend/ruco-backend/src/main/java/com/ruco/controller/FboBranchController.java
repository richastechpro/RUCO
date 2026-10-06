package com.ruco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ruco.request.FBOBranchRateRequest;
import com.ruco.request.FBOBranchRequest;
import com.ruco.response.FboBranchResponse;
import com.ruco.service.FboBranchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/fbo-branch")
public class FboBranchController {

	private final FboBranchService fboBranchService;

	@PostMapping("{fboId}")
	private ResponseEntity<?> create(@RequestBody FBOBranchRequest request, @PathVariable Long fboId) {
		fboBranchService.create(request, fboId);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping("{fboId}")
	private ResponseEntity<?> update(@RequestBody FBOBranchRequest request, @PathVariable Long fboId) {
		fboBranchService.update(request, fboId);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		fboBranchService.delete(id);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<?> getFboBranches(@PathVariable Long id) {
		FboBranchResponse branch = fboBranchService.getFboBranch(id);
		return ResponseEntity.ok().body(branch);
	}

	@GetMapping("valid/{fboId}")
	public ResponseEntity<?> getValidFboBranches(@PathVariable Long fboId) {
		List<FboBranchResponse> branch = fboBranchService.getValidFboBranches(fboId);
		return ResponseEntity.ok().body(branch);
	}

	@GetMapping("all-valid")
	public ResponseEntity<?> getAllValid() {
		List<FboBranchResponse> branch = fboBranchService.getAllValid();
		return ResponseEntity.ok().body(branch);
	}

	@GetMapping("all-valid/by-branch/{branchId}")
	public ResponseEntity<?> loadFboBranchByBranchId(@PathVariable Long branchId) {
		List<FboBranchResponse> branch = fboBranchService.loadFboBranchByBranchId(branchId);
		return ResponseEntity.ok().body(branch);
	}

	@GetMapping("download-format")
	public ResponseEntity<?> getSampleFormat() {
		return fboBranchService.getSampleFormat();
	}

	@PostMapping("/upload/{fboId}")
	public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file, @PathVariable Long fboId) {
		return fboBranchService.upload(file, fboId);
	}
	
	@PutMapping("update-rate")
	private ResponseEntity<?> updateRate(@RequestBody FBOBranchRateRequest request) {
		fboBranchService.createRateRequest(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
