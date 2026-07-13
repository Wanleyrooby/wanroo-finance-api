package com.wanroo.finance.mapper;

import com.wanroo.finance.dto.TransactionResponseDto;
import com.wanroo.finance.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public static TransactionResponseDto toResponse(Transaction transaction) {

        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDate(),
                transaction.getCategory().getId(),
                transaction.getCategory().getName(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}
