package com.simple.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.simple.demo.dto.request.CreateProductRequestDto;
import com.simple.demo.dto.request.UpdateProductRequestDto;
import com.simple.demo.persistence.entity.Product;
import com.simple.demo.persistence.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository repo;

	private void setEntity(Product entity, CreateProductRequestDto request) {
		entity.setName(request.getName());
		entity.setQuantity(request.getQuantity());
		entity.setPrice(request.getPrice());
	}

	@Transactional
	public void add(CreateProductRequestDto request) {
		Product entity = new Product();
		setEntity(entity, request);
		repo.save(entity);
	}

	@Transactional
	public void edit(UpdateProductRequestDto request) {
		Product product = getEntityById(request.getId());
		setEntity(product, request);
		repo.saveAndFlush(product);
	}

	public Product getEntityById(String id) {
		Optional<Product> product = repo.findById(id);
		if (product.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product tidak ada");
		}
		return product.get();
	}

	@Transactional
	public void deleteById(String id) {
		repo.delete(getEntityById(id));
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		ids.stream().forEach(this::deleteById);
	}

	public List<Product> getAll() {
		return repo.findAll();
	}
}
