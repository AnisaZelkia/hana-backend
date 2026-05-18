package com.simple.demo.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponseDto {

    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
}