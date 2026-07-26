package com.wanroo.finance.service;

import com.wanroo.finance.dto.DashboardFilterDto;
import com.wanroo.finance.dto.DashboardResponseDto;
import com.wanroo.finance.dto.MonthlySummaryDto;
import com.wanroo.finance.entity.TransactionType;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.repository.MonthlySummaryProjection;
import com.wanroo.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public DashboardResponseDto dashboard(DashboardFilterDto filter) {

        User user = authenticatedUserService.getAuthenticatedUser();

        BigDecimal totalIncome =
                transactionRepository.sumAmountByUserAndType(
                        user,
                        TransactionType.INCOME,
                        filter.startDate(),
                        filter.endDate()
                );

        BigDecimal totalExpense =
                transactionRepository.sumAmountByUserAndType(
                        user,
                        TransactionType.EXPENSE,
                        filter.startDate(),
                        filter.endDate()
                );

        Long totalTransactions =
                transactionRepository.countByUserAndPeriod(
                        user,
                        filter.startDate(),
                        filter.endDate()
                );

        BigDecimal balance =
                totalIncome.subtract(totalExpense);

        List<MonthlySummaryDto> monthlySummary =
                buildMonthlySummary(
                        transactionRepository.getMonthlySummary(
                                user,
                                filter.startDate(),
                                filter.endDate()
                        )
                );

        return new DashboardResponseDto(
                balance,
                totalIncome,
                totalExpense,
                totalTransactions,
                monthlySummary
        );
    }

    private List<MonthlySummaryDto> buildMonthlySummary(
            List<MonthlySummaryProjection> rows) {

        Map<String, MonthlyData> monthlyMap = new LinkedHashMap<>();

        for (MonthlySummaryProjection row : rows) {

            String key = row.getYear() + "-" + row.getMonth();

            // key = k = mes de um determinado ano
            MonthlyData data = monthlyMap.computeIfAbsent(
                    key,
                    k -> new MonthlyData(row.getYear(), row.getMonth())
            );

            if (row.getType() == TransactionType.INCOME) {
                data.income = row.getTotal();
            } else {
                data.expense = row.getTotal();
            }
        }

        return monthlyMap.values()
                .stream()
                .map(data -> new MonthlySummaryDto(
                        data.year,
                        data.month,
                        data.income,
                        data.expense,
                        data.income.subtract(data.expense)
                ))
                .toList();
    }

    private static class MonthlyData {

        private final Integer year;
        private final Integer month;

        private BigDecimal income = BigDecimal.ZERO;
        private BigDecimal expense = BigDecimal.ZERO;

        private MonthlyData(Integer year, Integer month) {
            this.year = year;
            this.month = month;
        }
    }
}
