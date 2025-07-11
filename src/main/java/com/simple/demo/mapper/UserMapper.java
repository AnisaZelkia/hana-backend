package com.simple.demo.mapper;

import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import com.simple.demo.dto.request.CreateUserRequestDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
	CreateUserRequestDto toDTO(User user);

	User toEntity(CreateUserRequestDto request);
}
