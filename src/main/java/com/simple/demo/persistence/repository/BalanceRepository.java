package com.simple.demo.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.simple.demo.persistence.entity.Balance;

public interface BalanceRepository extends JpaRepository<Balance, String>, JpaSpecificationExecutor<Balance> {

    Optional<Balance> findByAccountNumber(String accountNumber);
    Optional<Balance> findByUserId(String userId);
    boolean existsByAccountNumber(String accountNumber );
    boolean existsByUserId(String userId);

}
