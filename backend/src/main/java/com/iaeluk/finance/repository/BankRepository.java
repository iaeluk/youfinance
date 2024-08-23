package com.iaeluk.finance.repository;

import com.iaeluk.finance.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    List<Bank> findByUserId(Long appUserId);

}
