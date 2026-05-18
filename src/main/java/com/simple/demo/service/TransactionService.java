package com.simple.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.simple.demo.dto.request.TransferRequestDto;
import com.simple.demo.dto.response.TransactionResponseDto;
import com.simple.demo.persistence.entity.Balance;
import com.simple.demo.persistence.entity.Transaction;
import com.simple.demo.persistence.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private static final String STATUS_SUCCESS = "SUCCESS";
	private static final String ACCOUNT_STATUS_ACTIVE = "ACTIVE";
	private static final String TYPE_TRANSFER_OUT = "TRANSFER_OUT";
	private static final String TYPE_TRANSFER_IN = "TRANSFER_IN";
	private static final BigDecimal MINIMUM_TRANSFER_AMOUNT = BigDecimal.valueOf(10000);

	private final TransactionRepository transactionRepository;
	private final BalanceService balanceService;

	@Transactional
	public TransactionResponseDto transfer(TransferRequestDto request) {
		validateAmount(request.getAmount());

		Balance sender = balanceService.getCurrentUserBalanceEntity();
		Balance receiver = balanceService.getEntityByAccountNumber(request.getTargetAccountNumber());

		validateTransfer(sender, receiver, request.getAmount());

		sender.setBalanceTotal(sender.getBalanceTotal().subtract(request.getAmount()));
		receiver.setBalanceTotal(receiver.getBalanceTotal().add(request.getAmount()));

		balanceService.edit(sender);
		balanceService.edit(receiver);

		Transaction transaction = new Transaction();
		transaction.setSenderAccountNumber(sender.getAccountNumber());
		transaction.setReceiverAccountNumber(receiver.getAccountNumber());
		transaction.setAmount(request.getAmount());
		transaction.setStatus(STATUS_SUCCESS);
		transaction.setTransactionDate(LocalDateTime.now());
		transaction.setReferenceNo(generateReferenceNo());
		transaction.setDescription(buildDescription(receiver, request.getNote()));

		Transaction savedTransaction = transactionRepository.save(transaction);

		return toResponseDto(savedTransaction, sender.getAccountNumber());
	}

	public List<TransactionResponseDto> getCurrentUserHistory() {
		Balance currentBalance = balanceService.getCurrentUserBalanceEntity();
		String currentAccountNumber = currentBalance.getAccountNumber();

		return transactionRepository
				.findBySenderAccountNumberOrReceiverAccountNumberOrderByCreatedAtDesc(currentAccountNumber,
						currentAccountNumber)
				.stream().map(transaction -> toResponseDto(transaction, currentAccountNumber)).toList();
	}

	public TransactionResponseDto getDetailById(String id) {
		Balance currentBalance = balanceService.getCurrentUserBalanceEntity();
		String currentAccountNumber = currentBalance.getAccountNumber();

		Transaction transaction = getEntityById(id);

		boolean isOwner = currentAccountNumber.equals(transaction.getSenderAccountNumber())
				|| currentAccountNumber.equals(transaction.getReceiverAccountNumber());

		if (!isOwner) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to view this transaction");
		}

		return toResponseDto(transaction, currentAccountNumber);
	}

	public Transaction getEntityById(String id) {
		return transactionRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
	}

	public List<Transaction> getAll() {
		return transactionRepository.findAll();
	}

	@Transactional
	public void deleteById(String id) {
		transactionRepository.delete(getEntityById(id));
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		ids.forEach(this::deleteById);
	}

	private void validateTransfer(Balance sender, Balance receiver, BigDecimal amount) {
		if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender and receiver cannot be the same");
		}

		if (!ACCOUNT_STATUS_ACTIVE.equalsIgnoreCase(sender.getStatus())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender account is not active");
		}

		if (!ACCOUNT_STATUS_ACTIVE.equalsIgnoreCase(receiver.getStatus())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver account is not active");
		}

		if (sender.getBalanceTotal().compareTo(amount) < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
		}
	}

	private void validateAmount(BigDecimal amount) {
		if (amount == null || amount.compareTo(MINIMUM_TRANSFER_AMOUNT) < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum transfer amount is 10000");
		}
	}

	private String buildDescription(Balance receiver, String note) {
		if (note != null && !note.isBlank()) {
			return note;
		}

		return "Transfer to " + receiver.getAccountNumber();
	}

	private TransactionResponseDto toResponseDto(Transaction transaction, String currentAccountNumber) {
		boolean isOutgoing = currentAccountNumber.equals(transaction.getSenderAccountNumber());

		String transactionType = isOutgoing ? TYPE_TRANSFER_OUT : TYPE_TRANSFER_IN;

		String description = transaction.getDescription();

		if (description == null || description.isBlank()) {
			description = isOutgoing ? "Transfer to " + transaction.getReceiverAccountNumber()
					: "Transfer from " + transaction.getSenderAccountNumber();
		}

		return new TransactionResponseDto(transaction.getId(), transactionType, description, transaction.getAmount(),
				transaction.getCreatedAt(), transaction.getReceiverAccountNumber(),
				transaction.getSenderAccountNumber(), transaction.getStatus(), transaction.getReferenceNo());
	}

	private String generateReferenceNo() {
		return "TRX-" + System.currentTimeMillis();
	}
}