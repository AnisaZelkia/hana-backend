package com.simple.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.demo.dto.request.CreateUserRequestDto;
import com.simple.demo.dto.request.LoginRequestDto;
import com.simple.demo.dto.response.LoginResponseDto;
import com.simple.demo.dto.response.ResponseData;
import com.simple.demo.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<ResponseData<UserDetails>> registration(@RequestBody CreateUserRequestDto request) {
		UserDetails userDetails = authService.register(request);
		ResponseData<UserDetails> responseData = new ResponseData<>("User registered successfully", userDetails,
				HttpStatus.OK.value());
		return ResponseEntity.ok(responseData);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseData<LoginResponseDto>> login(@RequestBody LoginRequestDto request) {

		LoginResponseDto loginResponseDto = authService.login(request.getEmail(), request.getPassword());

		ResponseData<LoginResponseDto> responseData = new ResponseData<>("Login successful", loginResponseDto,
				HttpStatus.OK.value());

		return ResponseEntity.ok(responseData);
	}

}
