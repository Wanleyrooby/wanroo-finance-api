package com.wanroo.finance.dto;

import java.math.BigDecimal;

public record DashboardResponseDto(

        BigDecimal balance,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        Long totalTransactions

) {
}