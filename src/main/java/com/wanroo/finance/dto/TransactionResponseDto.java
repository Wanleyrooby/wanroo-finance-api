package com.wanroo.finance.dto;

import com.wanroo.finance.entity.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record TransactionResponseDto(
        Long id,
        String description,
        BigDecimal amount,
        TransactionType type,
        LocalDate date,
        Long categoryId,
        String categoryName,
        Instant createdAt,
        Instant updatedAt
) {
}
