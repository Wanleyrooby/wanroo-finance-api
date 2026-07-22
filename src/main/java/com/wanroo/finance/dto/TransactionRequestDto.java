package com.wanroo.finance.dto;

import com.wanroo.finance.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados para cadastro ou atualização de uma transação financeira")
public record TransactionRequestDto(

        @Schema(
                description = "Descrição da transação",
                example = "Compra no supermercado",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "A descrição é obrigatória.")
        String description,

        @Schema(
                description = "Valor da transação",
                example = "150.75",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O valor é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        BigDecimal amount,

        @Schema(
                description = "Tipo da transação",
                example = "EXPENSE",
                allowableValues = {"INCOME", "EXPENSE"},
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O tipo é obrigatório.")
        TransactionType type,

        @Schema(
                description = "Data da transação",
                example = "2026-07-22",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "A data é obrigatória.")
        LocalDate date,

        @Schema(
                description = "Identificador da categoria da transação",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "A categoria é obrigatória.")
        Long categoryId
) {
}