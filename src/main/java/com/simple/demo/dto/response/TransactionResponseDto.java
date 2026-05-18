package com.simple.demo.dto.response;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {

	private String id;
	private String transactionType;
	private String description;
	private BigDecimal amount;
	private ZonedDateTime transactionDate;
	private String targetAccountNumber;
	private String sourceAccountNumber;
	private String status;
	private String referenceNo;

}