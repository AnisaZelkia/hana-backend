package com.simple.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.simple.demo.persistence.entity.User;
import com.simple.demo.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository repo;
	private final BCryptPasswordEncoder bCrypt;
	
	public UserDetails getByEmail(String email) {
	    return repo.findByEmail(email)
	               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));
	}
	
	public User register(User user) {
		if(repo.existsByEmail(user.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
		}
		String passwordEncoder = bCrypt.encode(user.getPassword());
		user.setPassword(passwordEncoder);
		return repo.save(user);
	}

}
