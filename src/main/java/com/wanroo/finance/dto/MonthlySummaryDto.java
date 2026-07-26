package com.wanroo.finance.dto;

import java.math.BigDecimal;

public record MonthlySummaryDto(

        Integer year,
        Integer month,
        BigDecimal income,
        BigDecimal expense,
        BigDecimal balance

) {
}
