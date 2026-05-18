package com.simple.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDto {
	private String email;
	private String password;
	private String fullname;
	private String userRole;
	
	

}
