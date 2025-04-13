package com.simple.demo.persistence.entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "t_products")
@Entity
@SQLDelete(sql = "UPDATE t_products SET deleted_at = now() WHERE id=? AND version =?")
@Setter
@Getter
@SQLRestriction("deleted_at <> null")
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AuditableEntity {
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	@Column(name = "price", nullable = false)
	private Integer price;
}
