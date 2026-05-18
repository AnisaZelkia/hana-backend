package com.simple.demo.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.simple.demo.dto.request.UpdateBalanceRequestDto;
import com.simple.demo.dto.response.BalanceResponseDto;
import com.simple.demo.persistence.entity.Balance;
import com.simple.demo.persistence.entity.User;
import com.simple.demo.persistence.repository.BalanceRepository;
import com.simple.demo.util.CurrentUserUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BalanceService {

	private static final String DEFAULT_STATUS = "ACTIVE";
	private static final int ACCOUNT_NUMBER_LENGTH = 10;

	private final BalanceRepository balanceRepository;

	private final SecureRandom secureRandom = new SecureRandom();

	@Transactional
	public void add(User user) {
		validateCurrentUserDoesNotHaveBalance(user);
		Balance balance = createInitialBalance(user);
		balanceRepository.save(balance);
	}

	@Transactional
	public void edit(Balance balance) {
		balanceRepository.saveAndFlush(balance);
	}

	@Transactional
	public void edit(UpdateBalanceRequestDto request) {
		Balance balance = getEntityById(request.getId());
		updateBalance(balance, request);
		balanceRepository.saveAndFlush(balance);
	}

	public Balance getEntityById(String id) {
		return balanceRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance not found"));
	}

	public Balance getCurrentUserBalanceEntity() {
		String userId = CurrentUserUtil.getUserId();

		return balanceRepository.findByUserId(userId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance not found for current user"));
	}

	public BalanceResponseDto getCurrentUserBalance() {
		Balance balance = getCurrentUserBalanceEntity();
		return new BalanceResponseDto(balance.getAccountNumber(), balance.getUser().getFullname(),
				balance.getBalanceTotal());
	}

	public Balance getEntityByAccountNumber(String accountNumber) {
		return balanceRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Account number " + accountNumber + " not found"));
	}

	public List<Balance> getAll() {
		return balanceRepository.findAll();
	}

	@Transactional
	public void deleteById(String id) {
		Balance balance = getEntityById(id);
		balanceRepository.delete(balance);
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		balanceRepository.deleteAllById(ids);
	}

	private Balance createInitialBalance(User user) {
		Balance balance = new Balance();
		balance.setUser(user);
		balance.setAccountNumber(generateUniqueAccountNumber());
		balance.setBalanceTotal(BigDecimal.ZERO);
		balance.setStatus(DEFAULT_STATUS);
		return balance;
	}

	private void updateBalance(Balance balance, UpdateBalanceRequestDto request) {
		if (request.getBalance() != null) {
			balance.setBalanceTotal(request.getBalance());
		}
		if (request.getStatus() != null && !request.getStatus().isBlank()) {
			balance.setStatus(request.getStatus());
		}
	}

	private void validateCurrentUserDoesNotHaveBalance(User user) {
		String userId = user.getId();

		if (balanceRepository.existsByUserId(userId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current user already has a registered balance");
		}
	}

	private String generateUniqueAccountNumber() {
		String accountNumber;
		do {
			accountNumber = generateAccountNumber();
		} while (balanceRepository.existsByAccountNumber(accountNumber));
		return accountNumber;
	}

	private String generateAccountNumber() {
		StringBuilder accountNumber = new StringBuilder();

		for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
			accountNumber.append(secureRandom.nextInt(10));
		}

		return accountNumber.toString();
	}
}