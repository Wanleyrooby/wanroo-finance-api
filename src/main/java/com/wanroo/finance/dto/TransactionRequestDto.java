package com.wanroo.finance.dto;

import com.wanroo.finance.entity.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDto(

        @NotBlank(message = "A descrição é obrigatória.")
        String description,

        @NotNull(message = "O valor é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        BigDecimal amount,

        @NotNull(message = "O tipo é obrigatório.")
        TransactionType type,

        @NotNull(message = "A data é obrigatória.")
        LocalDate date,

        @NotNull(message = "A categoria é obrigatória.")
        Long categoryId
) {
}
