package com.simple.demo.persistence.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "t_balance")
@Entity
@SQLDelete(sql = "UPDATE t_balance SET deleted_at = now(), version = version + 1 WHERE id = ? AND version = ?")
@SQLRestriction("deleted_at IS NULL")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Balance extends AuditableEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "account_number", nullable = false, unique = true)
	private String accountNumber;

	@Column(name = "balance", nullable = false)
	private BigDecimal balanceTotal;

	@Column(name = "status", nullable = false)
	private String status;
}