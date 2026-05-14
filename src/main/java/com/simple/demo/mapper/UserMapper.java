package com.simple.demo.mapper;

import org.mapstruct.Mapper;
import com.simple.demo.dto.request.CreateUserRequestDto;
import com.simple.demo.persistence.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	CreateUserRequestDto toDTO(User user);
	User toEntity(CreateUserRequestDto request);
}
