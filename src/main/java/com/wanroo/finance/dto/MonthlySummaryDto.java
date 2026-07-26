package com.wanroo.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Resumo financeiro mensal utilizado no dashboard")
public record MonthlySummaryDto(

        @Schema(
                description = "Ano de referência",
                example = "2026"
        )
        Integer year,

        @Schema(
                description = "Mês de referência",
                example = "7"
        )
        Integer month,

        @Schema(
                description = "Total de receitas no mês",
                example = "8500.00"
        )
        BigDecimal income,

        @Schema(
                description = "Total de despesas no mês",
                example = "4200.50"
        )
        BigDecimal expense,

        @Schema(
                description = "Saldo do mês (receitas - despesas)",
                example = "4299.50"
        )
        BigDecimal balance

) {
}