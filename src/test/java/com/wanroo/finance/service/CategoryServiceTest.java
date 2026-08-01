package com.wanroo.finance.service;

import com.wanroo.finance.dto.CategoryRequestDto;
import com.wanroo.finance.dto.CategoryResponseDto;
import com.wanroo.finance.entity.Category;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.exception.CategoryNotFoundException;
import com.wanroo.finance.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private CategoryService categoryService;

    private User user;
    private Category category;
    private CategoryRequestDto requestDto;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .name("Wanley")
                .email("wanley@gmail.com")
                .build();

        category = Category.builder()
                .id(1L)
                .name("Alimentação")
                .description("Mercado")
                .user(user)
                .build();

        requestDto = new CategoryRequestDto("Alimentação", "Mercado");
    }

    @Test
    void shouldCreateCategorySuccessfully() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        CategoryResponseDto response = categoryService.create(requestDto);

        assertNotNull(response);
        assertEquals("Alimentação", response.name());

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldReturnPagedCategories() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Category> page = new PageImpl<>(List.of(category));

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByUser(user, pageable))
                .thenReturn(page);

        Page<CategoryResponseDto> response = categoryService.findAll(pageable);

        assertEquals(1, response.getTotalElements());

        verify(categoryRepository)
                .findByUser(user, pageable);
    }

    @Test
    void shouldReturnCategoryById() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(category));

        CategoryResponseDto response =
                categoryService.findById(1L);

        assertEquals(1L, response.id());
        assertEquals("Alimentação", response.name());
    }

    @Test
    void shouldThrowCategoryNotFoundException() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.findById(1L)
        );
    }

    @Test
    void shouldUpdateCategorySuccessfully() {

        CategoryRequestDto updateDto = new CategoryRequestDto("Lazer", "Cinema");

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(category));

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        CategoryResponseDto response = categoryService.update(1L, updateDto);

        assertNotNull(response);

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldDeleteCategorySuccessfully() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(category));

        categoryService.delete(1L);

        verify(categoryRepository).delete(category);
    }
}
