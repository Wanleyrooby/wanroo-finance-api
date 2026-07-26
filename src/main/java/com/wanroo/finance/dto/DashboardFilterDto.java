package com.wanroo.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Filtros para consulta do dashboard")
public record DashboardFilterDto(

        @Schema(
                description = "Data inicial do período",
                example = "2026-01-01"
        )
        LocalDate startDate,

        @Schema(
                description = "Data final do período",
                example = "2026-01-31"
        )
        LocalDate endDate
) {
}
