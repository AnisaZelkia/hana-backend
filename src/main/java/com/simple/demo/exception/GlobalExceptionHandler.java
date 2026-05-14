package com.simple.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.simple.demo.dto.response.ResponseData;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ResponseData<Object>> handleResponseStatusException(ResponseStatusException ex) {
		ResponseData<Object> response = new ResponseData<>(ex.getReason(),
				null, ex.getStatusCode().value());
		return ResponseEntity.status(ex.getStatusCode()).body(response);
	}
}