package com.wanroo.finance.dto;

import com.wanroo.finance.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Schema(description = "Dados de uma transação financeira")
public record TransactionResponseDto(

        @Schema(
                description = "Identificador único da transação",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Descrição da transação",
                example = "Compra no supermercado"
        )
        String description,

        @Schema(
                description = "Valor da transação",
                example = "150.75"
        )
        BigDecimal amount,

        @Schema(
                description = "Tipo da transação",
                example = "EXPENSE",
                allowableValues = {"INCOME", "EXPENSE"}
        )
        TransactionType type,

        @Schema(
                description = "Data da transação",
                example = "2026-07-22"
        )
        LocalDate date,

        @Schema(
                description = "Identificador da categoria",
                example = "1"
        )
        Long categoryId,

        @Schema(
                description = "Nome da categoria",
                example = "Alimentação"
        )
        String categoryName,

        @Schema(
                description = "Data e hora de criação da transação",
                example = "2026-07-22T10:15:30Z"
        )
        Instant createdAt,

        @Schema(
                description = "Data e hora da última atualização da transação",
                example = "2026-07-22T14:45:10Z"
        )
        Instant updatedAt
) {
}