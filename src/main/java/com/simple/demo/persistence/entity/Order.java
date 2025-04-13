package com.simple.demo.persistence.entity;

import java.time.LocalDate;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "t_orders")
@Entity
@SQLDelete(sql = "UPDATE t_orders SET deleted_at = now() WHERE id=? AND version =?")
@Setter
@Getter
@NoArgsConstructor
@SQLRestriction("deleted_at <> null")
@AllArgsConstructor
public class Order extends AuditableEntity {
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	@Column(name = "trx_number", nullable = false)
	private String trxNumber;
	@Column(name = "trx_date", nullable = false)
	private LocalDate trxDate;
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	@Column(name = "total_price", nullable = false)
	private Integer totalPrice;
	@Column(name = "status", nullable = false)
	private String status;
}
