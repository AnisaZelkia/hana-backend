package com.simple.demo.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.simple.demo.persistence.entity.Transaction;

public interface TransactionRepository
		extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {

	List<Transaction> findBySenderAccountNumberOrReceiverAccountNumberOrderByCreatedAtDesc(String senderAccountNumber,
			String receiverAccountNumber);

}
