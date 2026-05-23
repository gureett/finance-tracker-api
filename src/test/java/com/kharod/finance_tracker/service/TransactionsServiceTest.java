package com.kharod.finance_tracker.service;

import com.kharod.finance_tracker.dto.TransactionRequest;
import com.kharod.finance_tracker.exception.ResourceNotFoundException;
import com.kharod.finance_tracker.model.Role;
import com.kharod.finance_tracker.model.Transaction;
import com.kharod.finance_tracker.model.TransactionType;
import com.kharod.finance_tracker.model.User;
import com.kharod.finance_tracker.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;

    private User testUser;
    private Transaction testTransaction;
    private TransactionRequest testRequest;

    @BeforeEach
    void setUp() {
        // Create a fake user
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("password");
        testUser.setRole(Role.USER);

        // Create a fake transaction
        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setDescription("Coffee");
        testTransaction.setAmount(new BigDecimal("5.50"));
        testTransaction.setCategory("Food");
        testTransaction.setType(TransactionType.EXPENSE);
        testTransaction.setUser(testUser);

        // Create a fake request
        testRequest = new TransactionRequest();
        testRequest.setDescription("Coffee");
        testRequest.setAmount(new BigDecimal("5.50"));
        testRequest.setCategory("Food");
        testRequest.setType(TransactionType.EXPENSE);
    }

    @Test
    void getAllTransactions_shouldReturnUserTransactions() {
        when(repository.findByUser(testUser)).thenReturn(List.of(testTransaction));

        List<Transaction> result = service.getAllTransactions(testUser);

        assertEquals(1, result.size());
        assertEquals("Coffee", result.get(0).getDescription());
        verify(repository, times(1)).findByUser(testUser);
    }

    @Test
    void getTransactionById_shouldReturnTransaction_whenFound() {
        when(repository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testTransaction));

        Transaction result = service.getTransactionById(1L, testUser);

        assertEquals(1L, result.getId());
        assertEquals("Coffee", result.getDescription());
    }

    @Test
    void getTransactionById_shouldThrowException_whenNotFound() {
        when(repository.findByIdAndUser(99L, testUser)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getTransactionById(99L, testUser));
    }

    @Test
    void createTransaction_shouldSaveAndReturnTransaction() {
        when(repository.save(any(Transaction.class))).thenReturn(testTransaction);

        Transaction result = service.createTransaction(testRequest, testUser);

        assertNotNull(result);
        assertEquals("Coffee", result.getDescription());
        assertEquals(testUser, result.getUser());
        verify(repository, times(1)).save(any(Transaction.class));
    }

    @Test
    void updateTransaction_shouldUpdateAndReturnTransaction() {
        TransactionRequest updateRequest = new TransactionRequest();
        updateRequest.setDescription("Lunch");
        updateRequest.setAmount(new BigDecimal("12.00"));
        updateRequest.setCategory("Food");
        updateRequest.setType(TransactionType.EXPENSE);

        when(repository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testTransaction));
        when(repository.save(any(Transaction.class))).thenReturn(testTransaction);

        Transaction result = service.updateTransaction(1L, updateRequest, testUser);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Transaction.class));
    }

    @Test
    void deleteTransaction_shouldDeleteTransaction() {
        when(repository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testTransaction));
        doNothing().when(repository).delete(testTransaction);

        assertDoesNotThrow(() -> service.deleteTransaction(1L, testUser));
        verify(repository, times(1)).delete(testTransaction);
    }

    @Test
    void getByType_shouldReturnFilteredTransactions() {
        when(repository.findByUserAndType(testUser, TransactionType.EXPENSE))
                .thenReturn(List.of(testTransaction));

        List<Transaction> result = service.getByType(TransactionType.EXPENSE, testUser);

        assertEquals(1, result.size());
        assertEquals(TransactionType.EXPENSE, result.get(0).getType());
    }

    @Test
    void getByCategory_shouldReturnFilteredTransactions() {
        when(repository.findByUserAndCategory(testUser, "Food"))
                .thenReturn(List.of(testTransaction));

        List<Transaction> result = service.getByCategory("Food", testUser);

        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).getCategory());
    }
}