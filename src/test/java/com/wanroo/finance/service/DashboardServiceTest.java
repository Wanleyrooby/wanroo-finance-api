package com.wanroo.finance.service;

import com.wanroo.finance.dto.DashboardFilterDto;
import com.wanroo.finance.dto.DashboardResponseDto;
import com.wanroo.finance.dto.MonthlySummaryDto;
import com.wanroo.finance.entity.Role;
import com.wanroo.finance.entity.TransactionType;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.repository.MonthlySummaryProjection;
import com.wanroo.finance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private DashboardService dashboardService;

    private User user;
    private DashboardFilterDto filter;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .name("Wanley")
                .email("wanley@email.com")
                .password("123456")
                .role(Role.USER)
                .build();

        filter = new DashboardFilterDto(
                LocalDate.of(2026,1,1),
                LocalDate.of(2026,12,31)
        );
    }

    @Test
    void shouldReturnDashboardSuccessfully() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(transactionRepository.sumAmountByUserAndType(
                user,
                TransactionType.INCOME,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(new BigDecimal("10000"));

        when(transactionRepository.sumAmountByUserAndType(
                user,
                TransactionType.EXPENSE,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(new BigDecimal("3500"));

        when(transactionRepository.countByUserAndPeriod(
                user,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(12L);

        List<MonthlySummaryProjection> summary = List.of(
                new MonthlySummaryProjectionImpl(
                        2026,
                        1,
                        TransactionType.INCOME,
                        new BigDecimal("10000")
                ),
                new MonthlySummaryProjectionImpl(
                        2026,
                        1,
                        TransactionType.EXPENSE,
                        new BigDecimal("3500")
                )
        );

        when(transactionRepository.getMonthlySummary(
                user,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(summary);

        DashboardResponseDto response =
                dashboardService.dashboard(filter);

        assertNotNull(response);

        assertEquals(
                new BigDecimal("10000"),
                response.totalIncome());

        assertEquals(
                new BigDecimal("3500"),
                response.totalExpense());

        assertEquals(
                new BigDecimal("6500"),
                response.balance());

        assertEquals(
                12L,
                response.totalTransactions());

        assertEquals(
                1,
                response.monthlySummary().size());

        assertEquals(
                new BigDecimal("6500"),
                response.monthlySummary().getFirst().balance());
    }

    @Test
    void shouldReturnDashboardWithZeroValues() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(transactionRepository.sumAmountByUserAndType(
                user,
                TransactionType.INCOME,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(BigDecimal.ZERO);

        when(transactionRepository.sumAmountByUserAndType(
                user,
                TransactionType.EXPENSE,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(BigDecimal.ZERO);

        when(transactionRepository.countByUserAndPeriod(
                user,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(0L);

        when(transactionRepository.getMonthlySummary(
                user,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(List.of());

        DashboardResponseDto response =
                dashboardService.dashboard(filter);

        assertNotNull(response);

        assertEquals(BigDecimal.ZERO, response.totalIncome());
        assertEquals(BigDecimal.ZERO, response.totalExpense());
        assertEquals(BigDecimal.ZERO, response.balance());
        assertEquals(0L, response.totalTransactions());

        assertTrue(response.monthlySummary().isEmpty());
    }

    @Test
    void shouldGroupMonthlySummaryCorrectly() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(transactionRepository.sumAmountByUserAndType(
                user,
                TransactionType.INCOME,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(new BigDecimal("8000.00"));

        when(transactionRepository.sumAmountByUserAndType(
                user,
                TransactionType.EXPENSE,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(new BigDecimal("3000.00"));

        when(transactionRepository.countByUserAndPeriod(
                user,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(10L);

        List<MonthlySummaryProjection> summary =
                List.of(
                        new MonthlySummaryProjectionImpl(
                                2026,
                                1,
                                TransactionType.INCOME,
                                new BigDecimal("5000.00")
                        ),
                        new MonthlySummaryProjectionImpl(
                                2026,
                                1,
                                TransactionType.EXPENSE,
                                new BigDecimal("2000.00")
                        ),
                        new MonthlySummaryProjectionImpl(
                                2026,
                                2,
                                TransactionType.INCOME,
                                new BigDecimal("3000.00")
                        ),
                        new MonthlySummaryProjectionImpl(
                                2026,
                                2,
                                TransactionType.EXPENSE,
                                new BigDecimal("1000.00")
                        )
                );

        when(transactionRepository.getMonthlySummary(
                user,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(summary);

        DashboardResponseDto response =
                dashboardService.dashboard(filter);

        assertEquals(2, response.monthlySummary().size());

        MonthlySummaryDto january =
                response.monthlySummary().get(0);

        assertEquals(2026, january.year());
        assertEquals(1, january.month());
        assertEquals(new BigDecimal("5000.00"), january.income());
        assertEquals(new BigDecimal("2000.00"), january.expense());
        assertEquals(new BigDecimal("3000.00"), january.balance());

        MonthlySummaryDto february =
                response.monthlySummary().get(1);

        assertEquals(2026, february.year());
        assertEquals(2, february.month());
        assertEquals(new BigDecimal("3000.00"), february.income());
        assertEquals(new BigDecimal("1000.00"), february.expense());
        assertEquals(new BigDecimal("2000.00"), february.balance());
    }

    @Test
    void shouldUseFilterPeriodInRepositoryCalls() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(transactionRepository.sumAmountByUserAndType(
                user,
                TransactionType.INCOME,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(BigDecimal.ZERO);

        when(transactionRepository.sumAmountByUserAndType(
                user,
                TransactionType.EXPENSE,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(BigDecimal.ZERO);

        when(transactionRepository.countByUserAndPeriod(
                user,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(0L);

        when(transactionRepository.getMonthlySummary(
                user,
                filter.startDate(),
                filter.endDate()))
                .thenReturn(List.of());

        dashboardService.dashboard(filter);

        verify(transactionRepository)
                .sumAmountByUserAndType(
                        user,
                        TransactionType.INCOME,
                        filter.startDate(),
                        filter.endDate());

        verify(transactionRepository)
                .sumAmountByUserAndType(
                        user,
                        TransactionType.EXPENSE,
                        filter.startDate(),
                        filter.endDate());

        verify(transactionRepository)
                .countByUserAndPeriod(
                        user,
                        filter.startDate(),
                        filter.endDate());

        verify(transactionRepository)
                .getMonthlySummary(
                        user,
                        filter.startDate(),
                        filter.endDate());
    }

    private static class MonthlySummaryProjectionImpl
            implements MonthlySummaryProjection {

        private final Integer year;
        private final Integer month;
        private final TransactionType type;
        private final BigDecimal total;

        public MonthlySummaryProjectionImpl(
                Integer year,
                Integer month,
                TransactionType type,
                BigDecimal total) {

            this.year = year;
            this.month = month;
            this.type = type;
            this.total = total;

        }

        @Override
        public Integer getYear() {
            return year;
        }

        @Override
        public Integer getMonth() {
            return month;
        }

        @Override
        public TransactionType getType() {
            return type;
        }

        @Override
        public BigDecimal getTotal() {
            return total;
        }
    }
}
