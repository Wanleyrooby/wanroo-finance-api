package com.wanroo.finance.controller;

import com.wanroo.finance.dto.DashboardResponseDto;
import com.wanroo.finance.dto.TransactionRequestDto;
import com.wanroo.finance.dto.TransactionResponseDto;
import com.wanroo.finance.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> create(@RequestBody @Valid TransactionRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> findAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDto> dashboard() {
        return ResponseEntity.ok(transactionService.dashboard());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> update(@PathVariable Long id, @RequestBody @Valid TransactionRequestDto dto) {
        return ResponseEntity.ok(transactionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
