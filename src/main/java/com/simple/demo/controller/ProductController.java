package com.simple.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.demo.dto.request.CreateProductRequestDto;
import com.simple.demo.dto.request.UpdateProductRequestDto;
import com.simple.demo.persistence.entity.Product;
import com.simple.demo.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public ResponseEntity<Product> getProductById(@PathVariable String id) {
		return ResponseEntity.ok(productService.getEntityById(id));
	}

	@GetMapping("/all")
	public ResponseEntity<List<Product>> getAll() {
		return ResponseEntity.ok(productService.getAll());
	}

	@PostMapping
	public ResponseEntity<String> add(@RequestBody CreateProductRequestDto request) {
		productService.add(request);
		return ResponseEntity.ok("Berhasil Menambahkan Produk");
	}
	
	@PutMapping
	public ResponseEntity<String> edit(@RequestBody UpdateProductRequestDto request) {
		productService.edit(request);
		return ResponseEntity.ok("Berhasil Menambahkan Produk");
	}

	@DeleteMapping
	public ResponseEntity<String> deleteByIds(@RequestBody List<String> ids) {
		productService.deleteByIds(ids);
		return ResponseEntity.ok("Berhasil Delete Produk");
	}

}
