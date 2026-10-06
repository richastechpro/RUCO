package com.ruco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ruco.model.User;
import com.ruco.request.FBOBranchLoginRequest;
import com.ruco.response.FboBranchLoginResponse;
import com.ruco.service.FboBranchLoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/fbo-branch-login")
public class FboBranchLoginController {

	private final FboBranchLoginService fboBranchLoginService;

	@PostMapping("{fboId}")
	public ResponseEntity<?> create(@RequestBody FBOBranchLoginRequest request,@PathVariable Long fboId) {
		fboBranchLoginService.create(request,fboId);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping("{fboLoginId}")
	public ResponseEntity<?> getById(@PathVariable Long fboLoginId) {
		FboBranchLoginResponse login = fboBranchLoginService.getById(fboLoginId);
		return ResponseEntity.ok().body(login);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		fboBranchLoginService.delete(id);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "/pickupboys/{collectionId}")
	public ResponseEntity<?> getPickupBoys(@PathVariable Long collectionId) {
		List<User> pickupBoys = fboBranchLoginService.getPickupBoys(collectionId);
		return ResponseEntity.ok().body(pickupBoys);
	}
	
	@GetMapping("download-format/{fboId}")
	public ResponseEntity<?> getSampleFormat(@PathVariable Long fboId) {
		return fboBranchLoginService.getSampleFormat(fboId);
	}

	@PostMapping("/upload/{fboId}")
	public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file, @PathVariable Long fboId) {
		return fboBranchLoginService.upload(file, fboId);
	}

}
