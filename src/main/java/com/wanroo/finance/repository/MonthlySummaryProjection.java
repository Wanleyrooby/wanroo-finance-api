package com.wanroo.finance.repository;

import com.wanroo.finance.entity.TransactionType;

import java.math.BigDecimal;

public interface MonthlySummaryProjection {

    Integer getYear();
    Integer getMonth();
    TransactionType getType();
    BigDecimal getTotal();

}
