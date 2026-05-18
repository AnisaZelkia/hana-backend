package com.simple.demo.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			try {
				filterChain.doFilter(request, response);
			} catch (java.io.IOException | ServletException e) {
				e.printStackTrace();
			} 
			return;
		}

		String token = header.substring(7);
		String email = jwtService.extractUsername(token);

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			if (jwtService.validateToken(token)) {

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		try {
			filterChain.doFilter(request, response);
		} catch (java.io.IOException | ServletException e) {
			e.printStackTrace();
		}
	}
}