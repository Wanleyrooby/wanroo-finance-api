package com.wanroo.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Resumo financeiro do usuário")
public record DashboardResponseDto(

        @Schema(
                description = "Saldo atual (receitas - despesas)",
                example = "2350.75"
        )
        BigDecimal balance,

        @Schema(
                description = "Valor total das receitas",
                example = "5000.00"
        )
        BigDecimal totalIncome,

        @Schema(
                description = "Valor total das despesas",
                example = "2649.25"
        )
        BigDecimal totalExpense,

        @Schema(
                description = "Quantidade total de transações",
                example = "18"
        )
        Long totalTransactions,

        List<MonthlySummaryDto> monthlySummary

) {
}