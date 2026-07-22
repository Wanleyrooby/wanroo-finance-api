package com.wanroo.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para cadastro ou atualização de uma categoria")
public record CategoryRequestDto(

        @Schema(
                description = "Nome da categoria",
                example = "Alimentação",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Nome é obrigatório.")
        String name,

        @Schema(
                description = "Descrição da categoria",
                example = "Despesas com supermercado, restaurantes e lanches"
        )
        String description
) {
}