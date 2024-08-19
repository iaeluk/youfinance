package com.iaeluk.finance.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    @JsonIgnoreProperties({"transactions"})
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "expense_id")
    @JsonIgnoreProperties({"transactions"})
    private Expense expense;
}
