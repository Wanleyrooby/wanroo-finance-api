package com.wanroo.finance.dto;

import com.wanroo.finance.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Filtros para consulta de transações")
public record TransactionFilterDto(

        @Schema(
                description = "Data inicial do período",
                example = "2026-01-01"
        )
        LocalDate startDate,

        @Schema(
                description = "Data final do período",
                example = "2026-12-31"
        )
        LocalDate endDate,

        @Schema(
                description = "ID da categoria da transação",
                example = "1"
        )
        Long categoryId,

        @Schema(
                description = "Tipo da transação",
                example = "RECEITA",
                allowableValues = {"RECEITA", "DESPESA"}
        )
        TransactionType type
) {
}