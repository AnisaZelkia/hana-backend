package com.simple.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import com.simple.demo.dto.request.TransferRequestDto;
import com.simple.demo.dto.response.TransactionResponseDto;
import com.simple.demo.persistence.entity.Balance;
import com.simple.demo.persistence.entity.Transaction;
import com.simple.demo.persistence.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void transfer_shouldSuccess_whenRequestIsValid() {
        Balance sender = new Balance();
        sender.setAccountNumber("1111111111");
        sender.setBalanceTotal(BigDecimal.valueOf(100_000));
        sender.setStatus("ACTIVE");

        Balance receiver = new Balance();
        receiver.setAccountNumber("2222222222");
        receiver.setBalanceTotal(BigDecimal.valueOf(50_000));
        receiver.setStatus("ACTIVE");

        TransferRequestDto request = new TransferRequestDto();
        request.setTargetAccountNumber("2222222222");
        request.setAmount(BigDecimal.valueOf(20_000));
        request.setNote("Test transfer");

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId("trx-1");
        savedTransaction.setSenderAccountNumber("1111111111");
        savedTransaction.setReceiverAccountNumber("2222222222");
        savedTransaction.setAmount(BigDecimal.valueOf(20_000));
        savedTransaction.setStatus("SUCCESS");
        savedTransaction.setReferenceNo("TRX-123");
        savedTransaction.setDescription("Test transfer");

        when(balanceService.getCurrentUserBalanceEntity()).thenReturn(sender);
        when(balanceService.getEntityByAccountNumber("2222222222")).thenReturn(receiver);
        when(transactionRepository.save(org.mockito.ArgumentMatchers.any(Transaction.class)))
                .thenReturn(savedTransaction);

        TransactionResponseDto response = transactionService.transfer(request);

        assertNotNull(response);
        assertEquals("trx-1", response.getId());
        assertEquals("TRANSFER_OUT", response.getTransactionType());
        assertEquals(BigDecimal.valueOf(20_000), response.getAmount());

        assertEquals(BigDecimal.valueOf(80_000), sender.getBalanceTotal());
        assertEquals(BigDecimal.valueOf(70_000), receiver.getBalanceTotal());

        verify(balanceService).edit(sender);
        verify(balanceService).edit(receiver);
    }

    @Test
    void transfer_shouldThrowException_whenBalanceIsInsufficient() {
        Balance sender = new Balance();
        sender.setAccountNumber("1111111111");
        sender.setBalanceTotal(BigDecimal.valueOf(5_000));
        sender.setStatus("ACTIVE");

        Balance receiver = new Balance();
        receiver.setAccountNumber("2222222222");
        receiver.setBalanceTotal(BigDecimal.valueOf(50_000));
        receiver.setStatus("ACTIVE");

        TransferRequestDto request = new TransferRequestDto();
        request.setTargetAccountNumber("2222222222");
        request.setAmount(BigDecimal.valueOf(20_000));

        when(balanceService.getCurrentUserBalanceEntity()).thenReturn(sender);
        when(balanceService.getEntityByAccountNumber("2222222222")).thenReturn(receiver);

        assertThrows(ResponseStatusException.class, () -> transactionService.transfer(request));
    }

    @Test
    void transfer_shouldThrowException_whenSenderAndReceiverAreSame() {
        Balance sender = new Balance();
        sender.setAccountNumber("1111111111");
        sender.setBalanceTotal(BigDecimal.valueOf(100_000));
        sender.setStatus("ACTIVE");

        Balance receiver = new Balance();
        receiver.setAccountNumber("1111111111");
        receiver.setBalanceTotal(BigDecimal.valueOf(100_000));
        receiver.setStatus("ACTIVE");

        TransferRequestDto request = new TransferRequestDto();
        request.setTargetAccountNumber("1111111111");
        request.setAmount(BigDecimal.valueOf(20_000));

        when(balanceService.getCurrentUserBalanceEntity()).thenReturn(sender);
        when(balanceService.getEntityByAccountNumber("1111111111")).thenReturn(receiver);

        assertThrows(ResponseStatusException.class, () -> transactionService.transfer(request));
    }

    @Test
    void transfer_shouldThrowException_whenAmountBelowMinimum() {
        TransferRequestDto request = new TransferRequestDto();
        request.setTargetAccountNumber("2222222222");
        request.setAmount(BigDecimal.valueOf(5_000));

        assertThrows(ResponseStatusException.class, () -> transactionService.transfer(request));
    }

    @Test
    void getCurrentUserHistory_shouldReturnTransactions() {
        Balance currentBalance = new Balance();
        currentBalance.setAccountNumber("1111111111");

        Transaction transaction = new Transaction();
        transaction.setId("trx-1");
        transaction.setSenderAccountNumber("1111111111");
        transaction.setReceiverAccountNumber("2222222222");
        transaction.setAmount(BigDecimal.valueOf(20_000));
        transaction.setStatus("SUCCESS");
        transaction.setReferenceNo("TRX-123");
        transaction.setDescription("Test transfer");

        when(balanceService.getCurrentUserBalanceEntity()).thenReturn(currentBalance);
        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumberOrderByCreatedAtDesc(
                "1111111111",
                "1111111111"
        )).thenReturn(List.of(transaction));

        List<TransactionResponseDto> response = transactionService.getCurrentUserHistory();

        assertEquals(1, response.size());
        assertEquals("trx-1", response.get(0).getId());
        assertEquals("TRANSFER_OUT", response.get(0).getTransactionType());
    }
}