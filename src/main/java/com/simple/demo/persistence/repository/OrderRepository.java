package com.simple.demo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.simple.demo.persistence.entity.Order;

public interface OrderRepository    extends JpaRepository<Order, String>,
JpaSpecificationExecutor<Order> {

}
