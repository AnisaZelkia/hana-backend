package com.simple.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class UpdateProductRequestDto extends CreateProductRequestDto{
	private String id ;
	private int version;


}
