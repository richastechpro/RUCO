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

import com.ruco.request.SearchRequest;
import com.ruco.request.UserRequest;
import com.ruco.response.UserResponse;
import com.ruco.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

	private final UserService userService;

	@PostMapping("get")
	public ResponseEntity<?> get(@RequestBody SearchRequest request) {
		Page<?> items = userService.get(request);
		return ResponseEntity.ok().body(items);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody UserRequest user) {
		userService.create(user);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody UserRequest user) {
		userService.update(user);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		UserResponse user = userService.get(id);
		return ResponseEntity.ok().body(user);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "pickupboys/{collectionId}")
	public ResponseEntity<?> getPickupBoys(@PathVariable Long collectionId) {
		List<UserResponse> user = userService.getPickupBoys(collectionId);
		return ResponseEntity.ok().body(user);
	}

}
