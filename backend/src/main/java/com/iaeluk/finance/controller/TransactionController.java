package com.iaeluk.finance.controller;

import com.iaeluk.finance.model.Transaction;
import com.iaeluk.finance.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tx")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction newTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(newTransaction);
    }

    @GetMapping("/list")
    public List<Transaction> getAllTransactions() {
        return transactionService.getTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeTransaction(@PathVariable Long id) {
        if (transactionService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Transaction not found"));
        }

        transactionService.deleteTransaction(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Transaction deleted successfully"));
    }


    @GetMapping("/bank-id/{bankId}")
    public ResponseEntity<List<Transaction>> getTransactionsByBankId(@PathVariable Long bankId) {
        List<Transaction> transactions = transactionService.findTransactionsByBankId(bankId);
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @GetMapping("/expense-id/{expenseId}")
    public ResponseEntity<List<Transaction>> getTransactionsByExpenseId(@PathVariable Long expenseId) {
        List<Transaction> transactions = transactionService.findTransactionsByExpenseId(expenseId);
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @GetMapping("/bank/{name}")
    public ResponseEntity<List<Transaction>> getTransactionsByBankName(@PathVariable String name) {
        List<Transaction> transactions = transactionService.findTransactionsByBankName(name);
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @GetMapping("/expense/{expenseName}")
    public ResponseEntity<List<Transaction>> getTransactionsByExpenseName(@PathVariable String expenseName) {
        List<Transaction> transactions = transactionService.findTransactionsByExpenseName(expenseName);
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @GetMapping("/date/{month}-{year}")
    public ResponseEntity<List<Transaction>> getTransactionsByDate(@PathVariable int month, @PathVariable int year) {
        List<Transaction> transactions = transactionService.findTransactionsByMonthAndYear(month, year);
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @GetMapping("/bank-id/{bankId}/{month}-{year}")
    public ResponseEntity<List<Transaction>> getTransactionsByBankIdAndDate(@PathVariable Long bankId, @PathVariable int month, @PathVariable int year) {
        List<Transaction> transactions = transactionService.findTransactionsByBankIdAndMonthAndYear(bankId, month, year);
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @GetMapping("/expense-id/{expenseId}/{month}-{year}")
    public ResponseEntity<List<Transaction>> getTransactionsByExpenseIdAndDate(@PathVariable Long expenseId, @PathVariable int month, @PathVariable int year) {
        List<Transaction> transactions = transactionService.findTransactionsByExpenseIdAndMonthAndYear(expenseId, month, year);
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @GetMapping("/bank/{name}/{month}-{year}")
    public ResponseEntity<List<Transaction>> getTransactionsByBankNameAndDate(@PathVariable String name, @PathVariable int month, @PathVariable int year) {
        List<Transaction> transactions = transactionService.findTransactionsByBankNameAndMonthAndYear(name, month, year);
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @GetMapping("/expense-name/{expenseName}/{month}-{year}")
    public ResponseEntity<List<Transaction>> getTransactionsByExpenseNameAndDate(@PathVariable String expenseName, @PathVariable int month, @PathVariable int year) {
        List<Transaction> transactions = transactionService.findTransactionsByExpenseNameAndMonthAndYear(expenseName, month, year);
        return transactions.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transactions);
    }

    @GetMapping("/total/bank-id/{bankId}")
    public ResponseEntity<BigDecimal> getTotalAmountByBankId(@PathVariable Long bankId) {
        BigDecimal total = transactionService.findTotalAmountByBankId(bankId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/expense-id/{expenseId}")
    public ResponseEntity<BigDecimal> getTotalAmountByExpenseId(@PathVariable Long expenseId) {
        BigDecimal total = transactionService.findTotalAmountByExpenseId(expenseId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/bank/{name}")
    public ResponseEntity<BigDecimal> getTotalAmountByBankName(@PathVariable String name) {
        BigDecimal total = transactionService.findTotalAmountByBankName(name);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/expense/{expenseName}")
    public ResponseEntity<BigDecimal> getTotalAmountByExpenseName(@PathVariable String expenseName) {
        BigDecimal total = transactionService.findTotalAmountByExpenseName(expenseName);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/date/{month}-{year}")
    public ResponseEntity<BigDecimal> getTotalByDate(@PathVariable int month, @PathVariable int year) {
        BigDecimal total = transactionService.findTotalByMonthAndYear(month, year);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/bank-id/{bankId}/{month}-{year}")
    public ResponseEntity<BigDecimal> getTotalAmountByBankIdAndDate(@PathVariable Long bankId, @PathVariable int month, @PathVariable int year) {
        BigDecimal total = transactionService.findTotalAmountByBankIdAndMonthAndYear(bankId, month, year);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/expense-id/{expenseId}/{month}-{year}")
    public ResponseEntity<BigDecimal> getTotalAmountByExpenseIdAndDate(@PathVariable Long expenseId, @PathVariable int month, @PathVariable int year) {
        BigDecimal total = transactionService.findTotalAmountByExpenseIdAndMonthAndYear(expenseId, month, year);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/bank/{name}/{month}-{year}")
    public ResponseEntity<BigDecimal> getTotalAmountByBankNameAndDate(@PathVariable String name, @PathVariable int month, @PathVariable int year) {
        BigDecimal total = transactionService.findTotalAmountByBankNameAndMonthAndYear(name, month, year);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/expense-name/{expenseName}/{month}-{year}")
    public ResponseEntity<BigDecimal> getTotalAmountByExpenseNameAndDate(@PathVariable String expenseName, @PathVariable int month, @PathVariable int year) {
        BigDecimal total = transactionService.findTotalAmountByExpenseNameAndMonthAndYear(expenseName, month, year);
        return ResponseEntity.ok(total);
    }
}