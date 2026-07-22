package com.wanroo.finance.repository;

import com.wanroo.finance.entity.Category;
import com.wanroo.finance.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findByUser(User user, Pageable pageable);

    Optional<Category> findByIdAndUser(Long id, User user);
}
