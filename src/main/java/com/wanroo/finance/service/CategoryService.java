package com.wanroo.finance.service;

import com.wanroo.finance.dto.CategoryRequestDto;
import com.wanroo.finance.dto.CategoryResponseDto;
import com.wanroo.finance.entity.Category;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.mapper.CategoryMapper;
import com.wanroo.finance.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public CategoryResponseDto create(CategoryRequestDto dto) {

        User user = authenticatedUserService.getAuthenticatedUser();

        Category category = Category.builder()
                .name(dto.name())
                .description(dto.description())
                .user(user)
                .build();

        Category savedCategory = categoryRepository.save(category);

        return CategoryMapper.toResponse(savedCategory);
    }

    public List<CategoryResponseDto> findAll() {

        User user = authenticatedUserService.getAuthenticatedUser();

        return categoryRepository.findByUser(user)
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public CategoryResponseDto findById(Long id) {

        return CategoryMapper.toResponse(getCategoryById(id));
    }

    public CategoryResponseDto update(Long id, CategoryRequestDto dto) {

        Category category = getCategoryById(id);

        category.setName(dto.name());
        category.setDescription(dto.description());

        Category updatedCategory = categoryRepository.save(category);

        return CategoryMapper.toResponse(updatedCategory);
    }

    public void delete(Long id) {

        categoryRepository.delete(getCategoryById(id));
    }

    private Category getCategoryById(Long id) {

        User user = authenticatedUserService.getAuthenticatedUser();

        return categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Categoria não encontrada."));
    }
}