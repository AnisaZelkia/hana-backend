package com.simple.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.simple.demo.dto.request.CreateOrderRequestDto;
import com.simple.demo.dto.request.UpdateOrderRequestDto;
import com.simple.demo.persistence.entity.Order;
import com.simple.demo.persistence.entity.Product;
import com.simple.demo.persistence.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository repo;
	private final ProductService productService;

	private void setEntity(Order entity, CreateOrderRequestDto request) {
		Product product = productService.getEntityById(request.getProductId());
		if (product.getQuantity() < request.getQuantity()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stok tidak tersedia");
		}
		entity.setProduct(product);
		entity.setQuantity(request.getQuantity());
		entity.setTotalPrice(request.getQuantity() * product.getPrice());
		entity.setStatus(request.getStatus());
	}

	@Transactional
	public void add(CreateOrderRequestDto request) {
		Order entity = new Order();
		setEntity(entity, request);
		repo.save(entity);
	}

	@Transactional
	public void edit(UpdateOrderRequestDto request) {
		Optional<Order> existingOrder = repo.findById(request.getId());
		if (existingOrder.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order tidak ditemukan");
		}
		Order entity = existingOrder.get();
		setEntity(entity, request);
		repo.saveAndFlush(entity);
	}

	@Transactional
	public void deleteById(String id) {
		repo.delete(getEntityById(id));
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		ids.stream().forEach(this::deleteById);
	}

	public List<Order> getAll() {
		return repo.findAll();
	}

	public Order getEntityById(String id) {
		Optional<Order> order = repo.findById(id);
		if (order.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product tidak ada");
		}
		return order.get();
	}

}
