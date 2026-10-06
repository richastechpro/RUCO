package com.ruco.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruco.request.CompanyRequest;
import com.ruco.response.CompanyResponse;
import com.ruco.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/company")
public class CompanyController {

	private final CompanyService companyService;

	@GetMapping(value = "{limit}/{offset}")
	public ResponseEntity<?> get(@PathVariable Integer limit, @PathVariable Integer offset) {
		Page<?> items = companyService.get(limit, offset);
		return ResponseEntity.ok().body(items);
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<?> get(@PathVariable Long id) {
		CompanyResponse company = companyService.get(id);
		return ResponseEntity.ok().body(company);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		companyService.delete(id);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping
	private ResponseEntity<?> update(@RequestBody CompanyRequest company) {
		companyService.update(company);
		return ResponseEntity.ok(HttpStatus.OK);
	}

}
