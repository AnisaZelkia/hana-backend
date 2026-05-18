package com.simple.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.demo.dto.response.BalanceResponseDto;
import com.simple.demo.dto.response.ResponseData;
import com.simple.demo.service.BalanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

	private final BalanceService balanceService;

	@GetMapping("/balance")
	public ResponseEntity<ResponseData<BalanceResponseDto>> getCurrentUserBalance() {
		BalanceResponseDto response = balanceService.getCurrentUserBalance();
		ResponseData<BalanceResponseDto> responseData = new ResponseData<>("Balance retrieved successfully", response,
				HttpStatus.OK.value());
		return ResponseEntity.ok(responseData);
	}

}
