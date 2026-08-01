package com.wanroo.finance.service;

import com.wanroo.finance.dto.TransactionFilterDto;
import com.wanroo.finance.dto.TransactionRequestDto;
import com.wanroo.finance.dto.TransactionResponseDto;
import com.wanroo.finance.entity.*;
import com.wanroo.finance.exception.CategoryNotFoundException;
import com.wanroo.finance.exception.TransactionNotFoundException;
import com.wanroo.finance.repository.CategoryRepository;
import com.wanroo.finance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private TransactionService transactionService;

    private User user;
    private Category category;
    private Transaction transaction;
    private TransactionRequestDto requestDto;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .name("Wanley")
                .email("wanley@email.com")
                .password("123456")
                .role(Role.USER)
                .build();

        category = Category.builder()
                .id(1L)
                .name("Alimentação")
                .description("Mercado")
                .user(user)
                .build();

        transaction = Transaction.builder()
                .id(1L)
                .description("Supermercado")
                .amount(new BigDecimal("150.00"))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.of(2026, 7, 25))
                .category(category)
                .user(user)
                .createdAt(Instant.now())
                .build();

        requestDto = new TransactionRequestDto(
                "Supermercado",
                new BigDecimal("150.00"),
                TransactionType.EXPENSE,
                LocalDate.of(2026, 7, 25),
                1L
        );
    }

    @Test
    void shouldCreateTransactionSuccessfully() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(category));

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        TransactionResponseDto response =
                transactionService.create(requestDto);

        assertNotNull(response);
        assertEquals("Supermercado", response.description());
        assertEquals(new BigDecimal("150.00"), response.amount());
        assertEquals(TransactionType.EXPENSE, response.type());

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldSaveCorrectTransactionData() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(category));

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        transactionService.create(requestDto);

        ArgumentCaptor<Transaction> captor =
                ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();

        assertEquals(requestDto.description(), saved.getDescription());
        assertEquals(requestDto.amount(), saved.getAmount());
        assertEquals(requestDto.type(), saved.getType());
        assertEquals(requestDto.date(), saved.getDate());

        assertEquals(user, saved.getUser());
        assertEquals(category, saved.getCategory());
    }

    @Test
    void shouldThrowCategoryNotFoundWhenCreatingTransaction() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> transactionService.create(requestDto)
        );

        verify(transactionRepository, never())
                .save(any());
    }

    @Test
    void shouldReturnPagedTransactions() {

        TransactionFilterDto filter = new TransactionFilterDto(
                null,
                null,
                null,
                null
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<Transaction> page =
                new PageImpl<>(List.of(transaction));

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(transactionRepository.findAll(
                any(Specification.class),
                eq(pageable)))
                .thenReturn(page);

        Page<TransactionResponseDto> response =
                transactionService.findAll(filter, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getContent().size());

        TransactionResponseDto dto =
                response.getContent().getFirst();

        assertEquals(transaction.getId(), dto.id());
        assertEquals(transaction.getDescription(), dto.description());
        assertEquals(transaction.getAmount(), dto.amount());
        assertEquals(transaction.getType(), dto.type());

        verify(transactionRepository)
                .findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldReturnEmptyPage() {

        TransactionFilterDto filter = new TransactionFilterDto(
                null,
                null,
                null,
                null
        );

        Pageable pageable = PageRequest.of(0, 10);

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(transactionRepository.findAll(
                any(Specification.class),
                eq(pageable)))
                .thenReturn(Page.empty());

        Page<TransactionResponseDto> response =
                transactionService.findAll(filter, pageable);

        assertTrue(response.isEmpty());

        verify(transactionRepository)
                .findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldReturnTransactionById() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(transactionRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(transaction));

        TransactionResponseDto response =
                transactionService.findById(1L);

        assertNotNull(response);

        assertEquals(transaction.getId(), response.id());
        assertEquals(transaction.getDescription(), response.description());
        assertEquals(transaction.getAmount(), response.amount());
        assertEquals(transaction.getType(), response.type());
        assertEquals(transaction.getDate(), response.date());

        verify(transactionRepository)
                .findByIdAndUser(1L, user);
    }

    @Test
    void shouldThrowTransactionNotFoundException() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(transactionRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.findById(1L)
        );

        verify(transactionRepository)
                .findByIdAndUser(1L, user);
    }

    @Test
    void shouldUpdateTransactionSuccessfully() {

        TransactionRequestDto updateDto = new TransactionRequestDto(
                "Salário",
                new BigDecimal("5000.00"),
                TransactionType.INCOME,
                LocalDate.of(2026, 8, 1),
                1L
        );

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(category));

        when(transactionRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(transaction));

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        TransactionResponseDto response =
                transactionService.update(1L, updateDto);

        assertNotNull(response);

        ArgumentCaptor<Transaction> captor =
                ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository).save(captor.capture());

        Transaction updated = captor.getValue();

        assertEquals(updateDto.description(), updated.getDescription());
        assertEquals(updateDto.amount(), updated.getAmount());
        assertEquals(updateDto.type(), updated.getType());
        assertEquals(updateDto.date(), updated.getDate());
        assertEquals(category, updated.getCategory());
    }

    @Test
    void shouldThrowCategoryNotFoundWhenUpdating() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> transactionService.update(1L, requestDto)
        );

        verify(transactionRepository, never())
                .save(any(Transaction.class));
    }

    @Test
    void shouldThrowTransactionNotFoundWhenUpdating() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(category));

        when(transactionRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.update(1L, requestDto)
        );

        verify(transactionRepository, never())
                .save(any(Transaction.class));
    }

    @Test
    void shouldDeleteTransactionSuccessfully() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(transactionRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(transaction));

        transactionService.delete(1L);

        verify(transactionRepository)
                .delete(transaction);
    }

    @Test
    void shouldThrowTransactionNotFoundWhenDeleting() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(transactionRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.delete(1L)
        );

        verify(transactionRepository, never())
                .delete(any(Transaction.class));
    }
}
