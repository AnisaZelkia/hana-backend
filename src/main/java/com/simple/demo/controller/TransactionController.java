package com.simple.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.demo.dto.request.TransferRequestDto;
import com.simple.demo.dto.response.ResponseData;
import com.simple.demo.dto.response.TransactionResponseDto;
import com.simple.demo.persistence.entity.Transaction;
import com.simple.demo.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@PostMapping("/transfer")
	public ResponseEntity<ResponseData<TransactionResponseDto>> transfer(
			@Valid @RequestBody TransferRequestDto request) {
		TransactionResponseDto response = transactionService.transfer(request);

		return ResponseEntity.ok(new ResponseData<>("Transfer successful", response, HttpStatus.OK.value()));
	}

	@GetMapping("/history")
	public ResponseEntity<ResponseData<List<TransactionResponseDto>>> getCurrentUserHistory() {
		List<TransactionResponseDto> response = transactionService.getCurrentUserHistory();

		return ResponseEntity
				.ok(new ResponseData<>("Transaction history retrieved successfully", response, HttpStatus.OK.value()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseData<TransactionResponseDto>> getById(@PathVariable String id) {
		TransactionResponseDto response = transactionService.getDetailById(id);

		return ResponseEntity
				.ok(new ResponseData<>("Transaction detail retrieved successfully", response, HttpStatus.OK.value()));
	}

	@GetMapping
	public ResponseEntity<ResponseData<List<Transaction>>> getAll() {
		List<Transaction> response = transactionService.getAll();

		return ResponseEntity
				.ok(new ResponseData<>("Transactions retrieved successfully", response, HttpStatus.OK.value()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseData<String>> deleteById(@PathVariable String id) {
		transactionService.deleteById(id);

		return ResponseEntity.ok(new ResponseData<>("Transaction deleted successfully", null, HttpStatus.OK.value()));
	}

	@DeleteMapping
	public ResponseEntity<ResponseData<String>> deleteByIds(@RequestBody List<String> ids) {
		transactionService.deleteByIds(ids);

		return ResponseEntity.ok(new ResponseData<>("Transactions deleted successfully", null, HttpStatus.OK.value()));
	}
}