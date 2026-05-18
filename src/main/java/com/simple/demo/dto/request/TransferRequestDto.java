package com.simple.demo.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequestDto {

    @NotBlank(message = "Target account number is required")
    private String targetAccountNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "10000", message = "Minimum transfer amount is 10000")
    private BigDecimal amount;

    private String note;
}