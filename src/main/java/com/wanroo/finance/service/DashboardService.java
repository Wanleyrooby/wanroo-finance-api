package com.wanroo.finance.service;

import com.wanroo.finance.dto.DashboardResponseDto;
import com.wanroo.finance.entity.TransactionType;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserService authenticatedUserService;

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
}
