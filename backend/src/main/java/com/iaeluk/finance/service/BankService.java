package com.iaeluk.finance.service;

import com.iaeluk.finance.model.Bank;
import com.iaeluk.finance.model.Transaction;
import com.iaeluk.finance.repository.BankRepository;
import com.iaeluk.finance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Bank saveBank(Bank bank) {
        return bankRepository.save(bank);
    }

    public List<Bank> getBanks() {
        return bankRepository.findAll();
    }

    public Optional<Bank> findById(Long id) {
        return bankRepository.findById(id);
    }

    public Bank updateBank(Long id, Bank updatedBank) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        bank.setName(updatedBank.getName());

        BigDecimal totalAmount = transactionRepository.findTotalAmountByBankId(id);

        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }

        bank.setCreditLimit(updatedBank.getCreditLimit().subtract(totalAmount));

        return bankRepository.save(bank);
    }



    public void deleteBank(Long id) {
        bankRepository.deleteById(id);
    }
}
