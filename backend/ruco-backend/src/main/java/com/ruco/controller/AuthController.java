package com.ruco.controller;

import javax.security.auth.login.LoginException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruco.request.LoginRequest;
import com.ruco.request.UpdatePwdRequest;
import com.ruco.response.LoginResponse;
import com.ruco.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws LoginException {

		if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
			throw new IllegalArgumentException("Email or password cannot be null");
		}

		LoginResponse loginResponse = authService.login(loginRequest);
		return ResponseEntity.ok().body(loginResponse);
	}
	
	@PutMapping(value = "/pwd", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatePWD(@RequestBody UpdatePwdRequest request) throws LoginException {
		authService.updatePWD(request);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

}
