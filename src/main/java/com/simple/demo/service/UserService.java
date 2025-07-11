package com.simple.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.simple.demo.dto.request.CreateUserRequestDto;
import com.simple.demo.mapper.UserMapper;
import com.simple.demo.persistence.entity.User;
import com.simple.demo.persistence.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService implements UserDetailsService {

	private UserMapper mapper;
	private UserRepository repo;
	private BCryptPasswordEncoder bCrypt;

	public UserDetails getByEmail(String email) {
		return repo.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));
	}

	public User register(CreateUserRequestDto request) {
		if (repo.existsByEmail(request.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
		}
		/** This is using ecrypt using decrip */
		final String passwordEncoder = bCrypt.encode(request.getPassword());
		final User entity = new User();
		mapper.toEntity(request);
		entity.setPassword(passwordEncoder);
		return repo.save(entity);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Email is not register"));
	}
}
