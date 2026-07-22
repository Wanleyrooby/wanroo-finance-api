package com.wanroo.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de uma categoria")
public record CategoryResponseDto(

        @Schema(
                description = "Identificador único da categoria",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nome da categoria",
                example = "Alimentação"
        )
        String name,

        @Schema(
                description = "Descrição da categoria",
                example = "Despesas com supermercado, restaurantes e lanches"
        )
        String description
) {
}