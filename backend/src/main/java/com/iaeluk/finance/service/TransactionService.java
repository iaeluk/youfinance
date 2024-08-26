package com.iaeluk.finance.service;

import com.iaeluk.finance.model.Bank;
import com.iaeluk.finance.model.Expense;
import com.iaeluk.finance.model.Transaction;
import com.iaeluk.finance.model.User;
import com.iaeluk.finance.repository.BankRepository;
import com.iaeluk.finance.repository.ExpenseRepository;
import com.iaeluk.finance.repository.TransactionRepository;
import com.iaeluk.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        if (transaction.getBank() != null) {
            Bank bank = bankRepository.findById(transaction.getBank().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Bank not found"));

            if (transaction.getAmount().compareTo(bank.getCreditLimit()) > 0) {
                throw new IllegalArgumentException("Transaction amount exceeds bank limit");
            }

            bank.setCreditLimit(bank.getCreditLimit().subtract(transaction.getAmount()));
            bankRepository.save(bank);

        }

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions() {
        // Obter o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findBySub(authentication.getName());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Buscar todas as despesas associadas ao usuário
        List<Expense> expenses = expenseRepository.findByUserId(user.getId());

        // Buscar todos os bancos associados ao usuário
        List<Bank> banks = bankRepository.findByUserId(user.getId());

        // Buscar todas as transações associadas às despesas do usuário
        List<Transaction> expenseTransactions = expenses.stream()
                .flatMap(expense -> expense.getTransactions().stream())
                .collect(Collectors.toList());

        // Buscar todas as transações associadas aos bancos do usuário
        List<Transaction> bankTransactions = banks.stream()
                .flatMap(bank -> bank.getTransactions().stream())
                .toList();

        // Unir as transações de despesas e bancos
        expenseTransactions.addAll(bankTransactions);

        return expenseTransactions;
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Transactional
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        BigDecimal oldAmount = existingTransaction.getAmount();
        Bank oldBank = existingTransaction.getBank();
        Expense oldExpense = existingTransaction.getExpense();

        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setDate(updatedTransaction.getDate());

        if (updatedTransaction.getBank() != null) {
            Bank newBank = bankRepository.findById(updatedTransaction.getBank().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Bank not found"));

            if (oldBank != null && !oldBank.equals(newBank)) {
                oldBank.setCreditLimit(oldBank.getCreditLimit().add(oldAmount));
                bankRepository.save(oldBank);
            }

            if (updatedTransaction.getAmount().compareTo(newBank.getCreditLimit()) > 0) {
                throw new IllegalArgumentException("Transaction amount exceeds new Bank limit");
            }

            newBank.setCreditLimit(newBank.getCreditLimit().subtract(updatedTransaction.getAmount()));
            bankRepository.save(newBank);

            existingTransaction.setBank(newBank);
        } else if (oldBank != null) {
            oldBank.setCreditLimit(oldBank.getCreditLimit().add(oldAmount));
            bankRepository.save(oldBank);
            existingTransaction.setBank(null);
        }

        if (updatedTransaction.getExpense() != null) {
            Expense newExpense = expenseRepository.findById(updatedTransaction.getExpense().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

            existingTransaction.setExpense(newExpense);
        } else if (oldExpense != null) {
            existingTransaction.setExpense(null);
        }

        return transactionRepository.save(existingTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        BigDecimal oldAmount = transaction.getAmount();
        Bank bank = transaction.getBank();

        if (bank != null) {
            bank.setCreditLimit(bank.getCreditLimit().add(oldAmount));
            bankRepository.save(bank);
        }

        transactionRepository.deleteById(id);
    }

    public List<Transaction> findTransactionsByBankId(Long id) {
        return transactionRepository.findTransactionsByBankId(id);
    }

    public List<Transaction> findTransactionsByExpenseId(Long id) {
        return transactionRepository.findTransactionsByExpenseId(id);
    }

    public List<Transaction> findTransactionsByBankName(String bankName) {
        return transactionRepository.findTransactionsByBankName(bankName);
    }

    public List<Transaction> findTransactionsByExpenseName(String expenseName) {
        return transactionRepository.findTransactionsByExpenseName(expenseName);
    }

    public List<Transaction> findTransactionsByMonthAndYear(int month, int year) {
        return transactionRepository.findTransactionsByMonthAndYear(month, year);
    }

    public List<Transaction> findTransactionsByBankIdAndMonthAndYear(Long id, int month, int year) {
        return transactionRepository.findTransactionsByBankIdAndMonthAndYear(id, month, year);
    }

    public List<Transaction> findTransactionsByExpenseIdAndMonthAndYear(Long id, int month, int year) {
        return transactionRepository.findTransactionsByExpenseIdAndMonthAndYear(id, month, year);
    }

    public List<Transaction> findTransactionsByBankNameAndMonthAndYear(String bankName, int month, int year) {
        return transactionRepository.findTransactionsByBankNameAndMonthAndYear(bankName, month, year);
    }

    public List<Transaction> findTransactionsByExpenseNameAndMonthAndYear(String expenseName, int month, int year) {
        return transactionRepository.findTransactionsByExpenseNameAndMonthAndYear(expenseName, month, year);
    }

    public BigDecimal findTotalAmountByBankId(Long id) {
        return transactionRepository.findTotalAmountByBankId(id);
    }

    public BigDecimal findTotalAmountByExpenseId(Long id) {
        return transactionRepository.findTotalAmountByExpenseId(id);
    }

    public BigDecimal findTotalAmountByBankName(String bankName) {
        return transactionRepository.findTotalAmountByBankName(bankName);
    }

    public BigDecimal findTotalAmountByExpenseName(String expenseName) {
        return transactionRepository.findTotalAmountByExpenseName(expenseName);
    }

    public BigDecimal findTotalByMonthAndYear(int month, int year) {
        return transactionRepository.findTotalByMonthAndYear(month, year);
    }

    public BigDecimal findTotalAmountByBankIdAndMonthAndYear(Long id, int month, int year) {
        return transactionRepository.findTotalAmountByBankIdAndMonthAndYear(id, month, year);
    }

    public BigDecimal findTotalAmountByExpenseIdAndMonthAndYear(Long id, int month, int year) {
        return transactionRepository.findTotalAmountByExpenseIdAndMonthAndYear(id, month, year);
    }

    public BigDecimal findTotalAmountByBankNameAndMonthAndYear(String bankName, int month, int year) {
        return transactionRepository.findTotalAmountByBankNameAndMonthAndYear(bankName, month, year);
    }

    public BigDecimal findTotalAmountByExpenseNameAndMonthAndYear(String expenseName, int month, int year) {
        return transactionRepository.findTotalAmountByExpenseNameAndMonthAndYear(expenseName, month, year);
    }
}
