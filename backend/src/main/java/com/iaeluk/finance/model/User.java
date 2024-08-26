package com.iaeluk.finance.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String picture;
    private String sub;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Bank> banks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Expense> expenses;
}
