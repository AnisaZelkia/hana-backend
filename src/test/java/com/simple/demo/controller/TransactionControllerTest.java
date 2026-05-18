package com.simple.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.demo.dto.request.TransferRequestDto;
import com.simple.demo.dto.response.TransactionResponseDto;
import com.simple.demo.service.TransactionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void transfer_shouldReturnSuccessResponse() throws Exception {
        TransferRequestDto request = new TransferRequestDto();
        request.setTargetAccountNumber("2222222222");
        request.setAmount(BigDecimal.valueOf(20_000));
        request.setNote("Test transfer");

        TransactionResponseDto response = new TransactionResponseDto();
        response.setId("trx-1");
        response.setTransactionType("TRANSFER_OUT");
        response.setAmount(BigDecimal.valueOf(20_000));
        response.setStatus("SUCCESS");

        when(transactionService.transfer(org.mockito.ArgumentMatchers.any(TransferRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transfer successful"))
                .andExpect(jsonPath("$.data.id").value("trx-1"))
                .andExpect(jsonPath("$.data.transactionType").value("TRANSFER_OUT"))
                .andExpect(jsonPath("$.status").value(200));
    }
}