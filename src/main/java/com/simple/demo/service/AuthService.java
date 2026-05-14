package com.simple.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.simple.demo.dto.request.CreateUserRequestDto;
import com.simple.demo.dto.response.LoginResponseDto;
import com.simple.demo.mapper.UserMapper;
import com.simple.demo.persistence.entity.User;
import com.simple.demo.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository repo;
	private final BCryptPasswordEncoder bCrypt;
	private final JwtService jwtService;
	private final UserMapper mapper;

	public User register(CreateUserRequestDto request) {
		boolean isExist =repo.existsByEmail(request.getEmail());
		if (isExist) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
		}
		User entity = mapper.toEntity(request);
		entity.setPassword(bCrypt.encode(request.getPassword()));

		return repo.save(entity);
	}

	public LoginResponseDto login(String email, String password) {
		User user = repo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		boolean isValid = bCrypt.matches(password, user.getPassword());
		if (!isValid) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Password");

		}
		String token = jwtService.generateToken(email);
		return new LoginResponseDto(token, "Bearer", 3600L, email, user.getUserRole().name());

	}

	public Boolean isValidLogin(String email, String password) {
		User user = repo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		return bCrypt.matches(password, user.getPassword());
	}
}