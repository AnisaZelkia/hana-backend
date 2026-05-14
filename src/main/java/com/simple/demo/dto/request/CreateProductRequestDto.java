package com.simple.demo.dto.request;

import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequestDto {

	@NotBlank(message = "Name is required")
	private String name;

	@NotNull(message = "Quantity is required")
	@Min(value = 0, message = "Quantity must be greater than 0")
	private Integer quantity;

	@NotNull(message="Price is required")
	@Min(value =0, message ="Price must be greater than 0")
	private Integer price;


}
