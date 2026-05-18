package com.simple.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBalanceRequestDto {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Initial balance is required")
    @Min(value = 0, message = "Balance must be greater than or equal to 0")
    private BigDecimal balance;

    @NotBlank(message = "Status is required")
    private String status;
}