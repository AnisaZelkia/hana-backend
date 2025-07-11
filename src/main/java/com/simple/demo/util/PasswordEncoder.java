package com.simple.demo.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoder {
	/*
	 * Anotasi bean digunakan untuk diawal sudah di load di java context spring
	 * Sehingga bisa diinjek disemua kelas di spring yang membutuhkan (misalnya dengan autowired)
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
		
	}

}
