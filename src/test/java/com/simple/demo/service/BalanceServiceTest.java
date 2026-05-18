package com.simple.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import com.simple.demo.dto.response.BalanceResponseDto;
import com.simple.demo.persistence.entity.Balance;
import com.simple.demo.persistence.entity.User;
import com.simple.demo.persistence.repository.BalanceRepository;
import com.simple.demo.util.CurrentUserUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BalanceService balanceService;

    @Test
    void add_shouldCreateInitialBalance_whenCurrentUserHasNoBalance() {
        try (MockedStatic<CurrentUserUtil> currentUser = mockStatic(CurrentUserUtil.class)) {
            currentUser.when(CurrentUserUtil::getUserId).thenReturn("user-1");
            currentUser.when(CurrentUserUtil::getEmail).thenReturn("anisa@gmail.com");

            User user = new User();
            user.setEmail("anisa@gmail.com");
            user.setFullname("Anisa");

            when(balanceRepository.existsByUserId("user-1")).thenReturn(false);
            when(userService.getByEmail("anisa@gmail.com")).thenReturn(user);
            when(balanceRepository.existsByAccountNumber(org.mockito.ArgumentMatchers.anyString()))
                    .thenReturn(false);

            balanceService.add(user);

            verify(balanceRepository).save(org.mockito.ArgumentMatchers.any(Balance.class));
        }
    }


    @Test
    void getCurrentUserBalance_shouldReturnBalanceResponse() {
        try (MockedStatic<CurrentUserUtil> currentUser = mockStatic(CurrentUserUtil.class)) {
            currentUser.when(CurrentUserUtil::getUserId).thenReturn("user-1");

            User user = new User();
            user.setFullname("Anisa");

            Balance balance = new Balance();
            balance.setAccountNumber("1234567890");
            balance.setBalanceTotal(BigDecimal.valueOf(1_000_000));
            balance.setUser(user);

            when(balanceRepository.findByUserId("user-1"))
                    .thenReturn(Optional.of(balance));

            BalanceResponseDto response = balanceService.getCurrentUserBalance();

            assertNotNull(response);
            assertEquals("1234567890", response.getAccountNumber());
            assertEquals("Anisa", response.getAccountName());
            assertEquals(BigDecimal.valueOf(1_000_000), response.getBalance());
        }
    }

    @Test
    void getEntityByAccountNumber_shouldThrowException_whenAccountNumberNotFound() {
        when(balanceRepository.findByAccountNumber("9999999999"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> balanceService.getEntityByAccountNumber("9999999999")
        );
    }
}