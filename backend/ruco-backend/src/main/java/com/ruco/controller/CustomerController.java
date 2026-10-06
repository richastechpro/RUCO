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

import com.ruco.model.Customer;
import com.ruco.request.CustomerRequest;
import com.ruco.request.SearchRequest;
import com.ruco.response.CustomerResponse;
import com.ruco.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/customer")
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping("get")
	public ResponseEntity<?> get(@RequestBody SearchRequest request) {
		Page<?> items = customerService.get(request);
		return ResponseEntity.ok().body(items);
	}

	@GetMapping("valid")
	private ResponseEntity<?> getValid() {
		List<Customer> customers = customerService.getValid();
		return ResponseEntity.ok().body(customers);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody CustomerRequest request) {
		customerService.create(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody CustomerRequest request) {
		customerService.update(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		CustomerResponse user = customerService.get(id);
		return ResponseEntity.ok().body(user);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		customerService.delete(id);
		return ResponseEntity.ok(HttpStatus.OK);
	}

}
