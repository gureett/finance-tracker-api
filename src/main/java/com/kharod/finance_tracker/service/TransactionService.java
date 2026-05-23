package com.kharod.finance_tracker.service;

import com.kharod.finance_tracker.dto.TransactionRequest;
import com.kharod.finance_tracker.exception.ResourceNotFoundException;
import com.kharod.finance_tracker.model.Transaction;
import com.kharod.finance_tracker.model.TransactionType;
import com.kharod.finance_tracker.model.User;
import com.kharod.finance_tracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> getAllTransactions(User user) {
        return repository.findByUser(user);
    }

    public Transaction getTransactionById(Long id, User user) {
        return repository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with id: " + id));
    }

    public Transaction createTransaction(TransactionRequest request, User user) {
        Transaction transaction = new Transaction();
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setCategory(request.getCategory());
        transaction.setType(request.getType());
        transaction.setUser(user);
        return repository.save(transaction);
    }

    public Transaction updateTransaction(Long id, TransactionRequest request, User user) {
        Transaction transaction = getTransactionById(id, user);
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setCategory(request.getCategory());
        transaction.setType(request.getType());
        return repository.save(transaction);
    }

    public void deleteTransaction(Long id, User user) {
        Transaction transaction = getTransactionById(id, user);
        repository.delete(transaction);
    }

    public List<Transaction> getByType(TransactionType type, User user) {
        return repository.findByUserAndType(user, type);
    }

    public List<Transaction> getByCategory(String category, User user) {
        return repository.findByUserAndCategory(user, category);
    }
}