package com.kharod.finance_tracker.controller;

import com.kharod.finance_tracker.dto.TransactionRequest;
import com.kharod.finance_tracker.model.Transaction;
import com.kharod.finance_tracker.model.TransactionType;
import com.kharod.finance_tracker.model.User;
import com.kharod.finance_tracker.repository.UserRepository;
import com.kharod.finance_tracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;
    private final UserRepository userRepository;

    public TransactionController(TransactionService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getAllTransactions(getCurrentUser(userDetails)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getTransactionById(id, getCurrentUser(userDetails)));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Transaction>> getByType(
            @PathVariable TransactionType type,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getByType(type, getCurrentUser(userDetails)));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Transaction>> getByCategory(
            @PathVariable String category,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getByCategory(category, getCurrentUser(userDetails)));
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createTransaction(request, getCurrentUser(userDetails)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.updateTransaction(id, request, getCurrentUser(userDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        service.deleteTransaction(id, getCurrentUser(userDetails));
        return ResponseEntity.noContent().build();
    }
}