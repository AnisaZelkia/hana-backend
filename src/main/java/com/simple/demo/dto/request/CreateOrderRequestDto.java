package com.simple.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class CreateOrderRequestDto {
	private String productId;
	private String trxNumber;
	private Integer quantity;
	private String status;

}
