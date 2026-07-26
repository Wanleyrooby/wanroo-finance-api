package com.wanroo.finance.repository;

import com.wanroo.finance.entity.Transaction;
import com.wanroo.finance.entity.TransactionType;
import com.wanroo.finance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByIdAndUser(Long id, User user);

    @Query("""
        SELECT COUNT(t)
        FROM Transaction t
        WHERE t.user = :user
          AND (:startDate IS NULL OR t.date >= :startDate)
          AND (:endDate IS NULL OR t.date <= :endDate)
    """)
    Long countByUserAndPeriod(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user = :user
            AND t.type = :type
            """)
    BigDecimal sumAmountByUserAndType(@Param("user") User user, @Param("type") TransactionType transactionType);

    @Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM Transaction t
    WHERE t.user = :user
      AND t.type = :type
      AND (:startDate IS NULL OR t.date >= :startDate)
      AND (:endDate IS NULL OR t.date <= :endDate)
""")
    BigDecimal sumAmountByUserAndType(
            @Param("user") User user,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT
            YEAR(t.date) AS year,
            MONTH(t.date) AS month,
            t.type AS type,
            SUM(t.amount) AS total
        FROM Transaction t
        WHERE t.user = :user
          AND (:startDate IS NULL OR t.date >= :startDate)
          AND (:endDate IS NULL OR t.date <= :endDate)
        GROUP BY YEAR(t.date), MONTH(t.date), t.type
        ORDER BY YEAR(t.date), MONTH(t.date)
    """)
    List<MonthlySummaryProjection> getMonthlySummary(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
