package com.simple.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class CreateProductRequestDto {
	private String name;
	private Integer quantity;
	private Integer price;

}
