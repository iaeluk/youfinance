package com.iaeluk.finance.service;

import com.iaeluk.finance.model.Expense;
import com.iaeluk.finance.model.User;
import com.iaeluk.finance.repository.ExpenseRepository;
import com.iaeluk.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public Expense saveExpense(Expense expense) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findBySub(authentication.getName());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Expense newExpense = new Expense();
        newExpense.setName(expense.getName());
        newExpense.setUser(user);

        return expenseRepository.save(newExpense);
    }

    public List<Expense> getExpenses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findBySub(authentication.getName());
        return expenseRepository.findByUserId(user.getId());
    }

    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    public Expense updateExpense(Long id, Expense updatedExpense) {
        Optional<Expense> existingExpense = expenseRepository.findById(id);

        if (existingExpense.isPresent()) {
            Expense expense = existingExpense.get();
            expense.setName(updatedExpense.getName());

            return expenseRepository.save(expense);
        } else {
            throw new RuntimeException("Expense not found");
        }
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
