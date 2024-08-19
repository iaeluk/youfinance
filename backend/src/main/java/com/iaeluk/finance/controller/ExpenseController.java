package com.iaeluk.finance.controller;

import com.iaeluk.finance.model.Expense;
import com.iaeluk.finance.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping()
    public ResponseEntity<Expense> saveExpense(@RequestBody Expense expense) {
        Expense newExpense =  expenseService.saveExpense(expense);
        return ResponseEntity.ok(newExpense);
    }

    @GetMapping("/list")
    public List<Expense> getExpenses() {
        return expenseService.getExpenses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Optional<Expense> expense = expenseService.findById(id);
        return expense.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        Expense updatedExpense = expenseService.updateExpense(id, expense);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        // Verifica se a despesa existe
        if (expenseService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Expense not found"));
        }
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Expense deleted successfully"));
    }
}
