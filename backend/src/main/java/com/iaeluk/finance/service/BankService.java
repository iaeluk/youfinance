package com.iaeluk.finance.service;

import com.iaeluk.finance.model.Bank;
import com.iaeluk.finance.model.User;
import com.iaeluk.finance.repository.BankRepository;
import com.iaeluk.finance.repository.TransactionRepository;
import com.iaeluk.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public Bank saveBank(Bank bank) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findBySub(authentication.getName());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Bank newBank = new Bank();
        newBank.setName(bank.getName());
        newBank.setCreditLimit(bank.getCreditLimit());
        newBank.setUser(user);

        return bankRepository.save(newBank);
    }

    public List<Bank> getBanks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findBySub(authentication.getName());
        return bankRepository.findByUserId(user.getId());
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
