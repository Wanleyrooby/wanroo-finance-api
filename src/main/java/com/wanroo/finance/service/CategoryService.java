package com.wanroo.finance.service;

import com.wanroo.finance.dto.CategoryRequestDto;
import com.wanroo.finance.dto.CategoryResponseDto;
import com.wanroo.finance.entity.Category;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.mapper.CategoryMapper;
import com.wanroo.finance.repository.CategoryRepository;
import com.wanroo.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public CategoryResponseDto create(CategoryRequestDto dto) {

        User user = authenticatedUserService.getAuthenticatedUser();

        Category category = Category.builder()
                .name(dto.name())
                .description(dto.description())
                .user(user)
                .build();

        Category categorySaved = categoryRepository.save(category);

        return CategoryMapper.toResponse(categorySaved);
    }

    public List<CategoryResponseDto> findAll() {

        User user = authenticatedUserService.getAuthenticatedUser();

        return categoryRepository.findByUser(user)
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public CategoryResponseDto findById(Long id) {

        User user = authenticatedUserService.getAuthenticatedUser();

        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado.");
        }

        return CategoryMapper.toResponse(category);
    }

    public CategoryResponseDto update(Long id, CategoryRequestDto dto) {

        User user = authenticatedUserService.getAuthenticatedUser();

        Category category = Category.builder()
                .name(dto.name())
                .description(dto.description())
                .user(user)
                .build();

        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.toResponse(savedCategory);
    }

    public void delete(Long id) {

        User user = authenticatedUserService.getAuthenticatedUser();

        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        categoryRepository.delete(category);
    }
}
