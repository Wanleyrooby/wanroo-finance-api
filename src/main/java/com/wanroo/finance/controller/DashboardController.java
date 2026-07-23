package com.wanroo.finance.controller;

import com.wanroo.finance.dto.DashboardResponseDto;
import com.wanroo.finance.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints para consulta de indicadores financeiros")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(
            summary = "Dashboard financeiro",
            description = "Retorna os indicadores financeiros do usuário, como receitas, despesas e saldo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    public ResponseEntity<DashboardResponseDto> dashboard() {
        return ResponseEntity.ok(dashboardService.dashboard());
    }
}
