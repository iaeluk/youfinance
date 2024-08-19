package com.iaeluk.finance.service;

import com.iaeluk.finance.model.Bank;
import com.iaeluk.finance.model.Expense;
import com.iaeluk.finance.model.Transaction;
import com.iaeluk.finance.repository.BankRepository;
import com.iaeluk.finance.repository.ExpenseRepository;
import com.iaeluk.finance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

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
        return transactionRepository.findAll();
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
