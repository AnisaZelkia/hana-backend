package com.simple.demo.security;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/*
		 * http.authorizeHttpRequests(auth ->
		 * auth.anyRequest().authenticated()).formLogin(withDefaults());
		 */

		http.authorizeHttpRequests(auth -> auth.requestMatchers("/swagger-ui/**","/swagger-ui.html","/**","/api/**").permitAll().anyRequest().authenticated())
				.formLogin(withDefaults()).httpBasic(withDefaults());
		return http.build();
	}
}
