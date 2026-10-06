package com.ruco.controller;

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

import com.ruco.request.ChatRequest;
import com.ruco.request.LocationRequest;
import com.ruco.request.SearchRequest;
import com.ruco.request.TicketRequest;
import com.ruco.response.ChatResponse;
import com.ruco.response.TicketResponse;
import com.ruco.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ticket")
public class TicketController {

	private final TicketService ticketService;

	@PostMapping("get")
	public ResponseEntity<?> get(@RequestBody SearchRequest request) {
		Page<?> items = ticketService.get(request);
		return ResponseEntity.ok().body(items);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		TicketResponse response = ticketService.get(id);
		return ResponseEntity.ok().body(response);
	}

	@PostMapping
	private ResponseEntity<?> create(@RequestBody TicketRequest request) {
		ticketService.create(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping
	private ResponseEntity<?> update(@RequestBody LocationRequest request) {
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	private ResponseEntity<?> delete(@PathVariable Long id) {
		ticketService.delete(id);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@PostMapping("send-msg")
	private ResponseEntity<?> sendMsg(@RequestBody ChatRequest request) {
		ChatResponse message = ticketService.sendMsg(request);
		return ResponseEntity.ok().body(message);
	}
	
	@GetMapping("close/{id}")
	public ResponseEntity<?> closeTicket(@PathVariable Long id) {
		ticketService.closeTicket(id);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

}
