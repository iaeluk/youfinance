package com.iaeluk.finance.repository;

import com.iaeluk.finance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.bank.id = :bankId")
    List<Transaction> findTransactionsByBankId(@Param("bankId") Long bankId);

    @Query("SELECT t FROM Transaction t WHERE t.expense.id = :expenseId")
    List<Transaction> findTransactionsByExpenseId(@Param("expenseId") Long expenseId);

    @Query("SELECT t FROM Transaction t JOIN t.bank b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Transaction> findTransactionsByBankName(@Param("name") String name);

    @Query("SELECT t FROM Transaction t JOIN t.expense e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :expenseName, '%'))")
    List<Transaction> findTransactionsByExpenseName(@Param("expenseName") String expenseName);

    @Query("SELECT t FROM Transaction t WHERE MONTH(t.date) = :month AND YEAR(t.date) = :year")
    List<Transaction> findTransactionsByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT t FROM Transaction t WHERE t.bank.id = :bankId AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    List<Transaction> findTransactionsByBankIdAndMonthAndYear(@Param("bankId") Long bankId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT t FROM Transaction t WHERE t.expense.id = :expenseId AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    List<Transaction> findTransactionsByExpenseIdAndMonthAndYear(@Param("expenseId") Long expenseId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT t FROM Transaction t JOIN t.bank b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')) AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    List<Transaction> findTransactionsByBankNameAndMonthAndYear(@Param("name") String name, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT t FROM Transaction t JOIN t.expense e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :expenseName, '%')) AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    List<Transaction> findTransactionsByExpenseNameAndMonthAndYear(@Param("expenseName") String expenseName, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.bank.id = :bankId")
    BigDecimal findTotalAmountByBankId(@Param("bankId") Long bankId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.expense.id = :expenseId")
    BigDecimal findTotalAmountByExpenseId(@Param("expenseId") Long expenseId);

    @Query("SELECT SUM(t.amount) FROM Transaction t JOIN t.bank b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    BigDecimal findTotalAmountByBankName(@Param("name") String name);

    @Query("SELECT SUM(t.amount) FROM Transaction t JOIN t.expense e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :expenseName, '%'))")
    BigDecimal findTotalAmountByExpenseName(@Param("expenseName") String expenseName);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE MONTH(t.date) = :month AND YEAR(t.date) = :year")
    BigDecimal findTotalByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.bank.id = :bankId AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    BigDecimal findTotalAmountByBankIdAndMonthAndYear(@Param("bankId") Long bankId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.expense.id = :expenseId AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    BigDecimal findTotalAmountByExpenseIdAndMonthAndYear(@Param("expenseId") Long expenseId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT SUM(t.amount) FROM Transaction t JOIN t.bank b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')) AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    BigDecimal findTotalAmountByBankNameAndMonthAndYear(@Param("name") String name, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT SUM(t.amount) FROM Transaction t JOIN t.expense e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :expenseName, '%')) AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    BigDecimal findTotalAmountByExpenseNameAndMonthAndYear(@Param("expenseName") String expenseName, @Param("month") Integer month, @Param("year") Integer year);
}
