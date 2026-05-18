package com.simple.demo.mapper;

import org.mapstruct.Mapper;

import com.simple.demo.dto.request.CreateBalanceRequestDto;
import com.simple.demo.persistence.entity.Balance;

@Mapper(componentModel = "spring")
public interface BalanceMapper {
	Balance toEntity(CreateBalanceRequestDto request);
}
