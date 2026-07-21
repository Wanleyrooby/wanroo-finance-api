package com.wanroo.finance.service;

import com.wanroo.finance.dto.DashboardResponseDto;
import com.wanroo.finance.dto.TransactionRequestDto;
import com.wanroo.finance.dto.TransactionResponseDto;
import com.wanroo.finance.entity.Category;
import com.wanroo.finance.entity.Transaction;
import com.wanroo.finance.entity.TransactionType;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.exception.CategoryNotFoundException;
import com.wanroo.finance.exception.TransactionNotFoundException;
import com.wanroo.finance.mapper.TransactionMapper;
import com.wanroo.finance.repository.CategoryRepository;
import com.wanroo.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final CategoryRepository categoryRepository;

    public TransactionResponseDto create(TransactionRequestDto dto) {

        User user = authenticatedUserService.getAuthenticatedUser();

        Category category = categoryRepository.findByIdAndUser(dto.categoryId(), user)
                .orElseThrow(CategoryNotFoundException::new);

        Transaction transaction = Transaction.builder()
                .description(dto.description())
                .amount(dto.amount())
                .type(dto.type())
                .date(dto.date())
                .user(user)
                .category(category)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.toResponse(savedTransaction);
    }

    public List<TransactionResponseDto> findAll() {

        User user = authenticatedUserService.getAuthenticatedUser();

        return transactionRepository.findByUser(user)
                .stream()
                .map(TransactionMapper::toResponse)
                .toList();
    }

    public TransactionResponseDto findById(Long id) {
        return TransactionMapper.toResponse(getTransactionById(id));
    }

    public TransactionResponseDto update(Long id, TransactionRequestDto dto) {

        User user =  authenticatedUserService.getAuthenticatedUser();

        Category category = categoryRepository.findByIdAndUser(dto.categoryId(), user)
                .orElseThrow(CategoryNotFoundException::new);

        Transaction transaction = getTransactionById(id);

        transaction.setDescription(dto.description());
        transaction.setAmount(dto.amount());
        transaction.setType(dto.type());
        transaction.setDate(dto.date());
        transaction.setCategory(category);

        Transaction updatedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.toResponse(updatedTransaction);
    }

    public DashboardResponseDto dashboard() {

        User user = authenticatedUserService.getAuthenticatedUser();

        BigDecimal totalIncome =
                transactionRepository.sumAmountByUserAndType(
                        user,
                        TransactionType.INCOME
                );

        BigDecimal totalExpense =
                transactionRepository.sumAmountByUserAndType(
                        user,
                        TransactionType.EXPENSE
                );

        Long totalTransactions =
                transactionRepository.countByUser(user);

        BigDecimal balance =
                totalIncome.subtract(totalExpense);

        return new DashboardResponseDto(
                balance,
                totalIncome,
                totalExpense,
                totalTransactions
        );
    }

    public void delete(Long id) {
        transactionRepository.delete(getTransactionById(id));
    }

    private Transaction getTransactionById(Long id) {

        User user = authenticatedUserService.getAuthenticatedUser();

        return transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(TransactionNotFoundException::new);
    }
}
