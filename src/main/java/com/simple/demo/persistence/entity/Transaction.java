package com.simple.demo.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "t_transactions")
@Entity
@SQLDelete(sql = "UPDATE t_transactions SET deleted_at = now(), version = version + 1 WHERE id = ? AND version = ?")
@SQLRestriction("deleted_at IS NULL")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends AuditableEntity {

	@Column(name = "reference_no", nullable = false, unique = true)
	private String referenceNo;

	@Column(name = "sender_account_number", nullable = false)
	private String senderAccountNumber;

	@Column(name = "receiver_account_number", nullable = false)
	private String receiverAccountNumber;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@Column(name = "transaction_date", nullable = false)
	private LocalDateTime transactionDate;

	@Column(name = "status", nullable = false)
	private String status;
	
	

	@Column(name = "description", nullable = true)
	private String description;
	
	
	
}