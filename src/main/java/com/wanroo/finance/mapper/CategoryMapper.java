package com.wanroo.finance.mapper;

import com.wanroo.finance.dto.CategoryRequestDto;
import com.wanroo.finance.dto.CategoryResponseDto;
import com.wanroo.finance.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public static Category toEntity(CategoryRequestDto dto) {

        return Category.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
    }

    public static CategoryResponseDto toResponse(Category category) {

        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
