package com.wanroo.finance.repository;

import com.wanroo.finance.entity.Transaction;
import com.wanroo.finance.entity.TransactionType;
import com.wanroo.finance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);

    Optional<Transaction> findByIdAndUser(Long id, User user);

    Long countByUser(User user);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user = :user
            AND t.type = :type
            """)
    BigDecimal sumAmountByUserAndType(
            @Param("user") User user,
            @Param("type") TransactionType transactionType);
}
