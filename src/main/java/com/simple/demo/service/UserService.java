package com.simple.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.simple.demo.persistence.entity.User;
import com.simple.demo.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	private final UserRepository repo;

	public User getByEmail(String email) {
		return repo.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		return repo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Email is not registered"));
	}
}
