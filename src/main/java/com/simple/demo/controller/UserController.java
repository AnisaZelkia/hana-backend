package com.simple.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.demo.dto.ResponseData;
import com.simple.demo.persistence.entity.User;
import com.simple.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<ResponseData<UserDetails>> registration(@RequestBody User user) {
		UserDetails userDetails = userService.register(user);
		ResponseData<UserDetails> responseData = new ResponseData<>("User registered successfully", userDetails,
				HttpStatus.OK.value());
		return ResponseEntity.ok(responseData);
	}
}
