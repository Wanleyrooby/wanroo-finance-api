package com.wanroo.finance.specification;

import com.wanroo.finance.dto.TransactionFilterDto;
import com.wanroo.finance.entity.Transaction;
import com.wanroo.finance.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transaction> byFilters(User user, TransactionFilterDto filter) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("user"), user));

            if (filter.startDate() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("date"),
                                filter.startDate()
                        )
                );
            }

            if (filter.endDate() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("date"),
                                filter.endDate()
                        )
                );
            }

            if (filter.categoryId() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("category").get("id"),
                                filter.categoryId()
                        )
                );
            }

            if (filter.type() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("type"), filter.type()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
