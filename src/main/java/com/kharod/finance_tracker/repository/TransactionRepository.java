package com.kharod.finance_tracker.repository;

import com.kharod.finance_tracker.model.Transaction;
import com.kharod.finance_tracker.model.TransactionType;
import com.kharod.finance_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserAndType(User user, TransactionType type);
    List<Transaction> findByUserAndCategory(User user, String category);
    Optional<Transaction> findByIdAndUser(Long id, User user);
}