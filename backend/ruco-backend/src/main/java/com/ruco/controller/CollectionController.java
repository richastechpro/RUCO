package com.ruco.controller;

import java.io.IOException;
import java.text.ParseException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.postmarkapp.postmark.client.exception.PostmarkException;
import com.ruco.model.CollectionItem;
import com.ruco.request.BulkCollecionRequest;
import com.ruco.request.CollectionAssignRequest;
import com.ruco.request.CollectionCollectRequest;
import com.ruco.request.CollectionItemRequest;
import com.ruco.request.InventoryOutRequest;
import com.ruco.request.SearchRequest;
import com.ruco.response.BulkCollectionResponse;
import com.ruco.response.CertficateResponse;
import com.ruco.response.CollectionResponse;
import com.ruco.service.CollectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/collection")
public class CollectionController {

	private final CollectionService collectionService;

	@PostMapping("get")
	public ResponseEntity<?> get(@RequestBody SearchRequest request) {
		Page<?> items = collectionService.get(request);
		return ResponseEntity.ok().body(items);
	}

	@PostMapping("single")
	private ResponseEntity<?> createSingleCollection(@RequestBody CollectionItemRequest request) {
		collectionService.createSingleCollection(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PutMapping("/assign")
	public ResponseEntity<?> assignPickupBoy(@RequestBody CollectionAssignRequest request) {
		collectionService.assignPickupBoy(request);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@PutMapping("/collect")
	public ResponseEntity<?> collect(@RequestBody CollectionCollectRequest request) throws ParseException {
		collectionService.collect(request);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@PostMapping("bulk")
	private ResponseEntity<?> createBulkCollection(@RequestBody BulkCollecionRequest request) {
		collectionService.createBulkCollection(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "bulk/{collectionId}")
	public ResponseEntity<?> getBulk(@PathVariable Long collectionId) {
		BulkCollectionResponse requests = collectionService.getBulk(collectionId);
		return ResponseEntity.ok().body(requests);
	}

	@DeleteMapping(value = "{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		collectionService.delete(id);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@DeleteMapping(value = "inventory-out/{id}")
	public ResponseEntity<?> deleteInventoryOut(@PathVariable Long id) {
		collectionService.deleteInventoryOut(id);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@GetMapping(value = "certficate/{requestId}")
	public ResponseEntity<?> getCertificate(@PathVariable Long requestId) throws ParseException {
		CertficateResponse response = collectionService.getCertificate(requestId);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping(value = "bulk-collection-name")
	public ResponseEntity<?> getBulkCollectionName() {
		String response = collectionService.getBulkCollectionName();
		return ResponseEntity.ok().body(new String[] { response });
	}

	@PutMapping("upload")
	public ResponseEntity<?> uploadFiles(@RequestParam(required = false) List<MultipartFile> files,
			@RequestParam(required = false) Long requestId, @RequestParam(required = false) Long collectionId,
			@RequestParam String type, @RequestParam Float collectedQty, @RequestParam String collectedOn,
			@RequestParam String invoiceNo, @RequestParam String descr) {
		collectionService.upload(requestId, files, collectionId, type, collectedQty, collectedOn, invoiceNo, descr);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@PostMapping("inventory-in/{collectionId}")
	private ResponseEntity<?> createInventoryIn(@PathVariable Long collectionId) {
		collectionService.createInventoryIn(collectionId);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("inventory-out")
	private ResponseEntity<?> createInventoryOut(@RequestBody InventoryOutRequest request) {
		collectionService.createInventoryOut(request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping(value = "inventory-in/{limit}/{offset}")
	public ResponseEntity<?> getInventotyIn(@PathVariable Integer limit, @PathVariable Integer offset) {
		Page<?> items = collectionService.getInventotyIn(limit, offset);
		return ResponseEntity.ok().body(items);
	}

	@GetMapping(value = "inventory-out/{limit}/{offset}")
	public ResponseEntity<?> getInventotyOut(@PathVariable Integer limit, @PathVariable Integer offset) {
		Page<?> items = collectionService.getInventotyOut(limit, offset);
		return ResponseEntity.ok().body(items);
	}

	@GetMapping("send-otp")
	private ResponseEntity<?> requestOTP() throws IOException, PostmarkException {
		collectionService.requestOTP();
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@GetMapping("verify-otp")
	private ResponseEntity<?> verifyOTP(@RequestParam(required = true) Integer otp) {
		collectionService.verifyOTP(otp);
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	@GetMapping(value = "assigned/{limit}/{offset}")
	public ResponseEntity<?> getAssignedCollection(@PathVariable Integer limit, @PathVariable Integer offset) {
		Page<?> items = collectionService.getAssignedCollection(limit, offset);
		return ResponseEntity.ok().body(items);
	}

	@GetMapping("{collectionId}")
	private ResponseEntity<?> getCollectionData(@PathVariable Long collectionId) {
		CollectionResponse response = collectionService.getCollectionData(collectionId);
		return ResponseEntity.ok().body(response);
	}

}
