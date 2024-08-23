package com.iaeluk.finance.service;

import com.iaeluk.finance.model.Bank;
import com.iaeluk.finance.model.Transaction;
import com.iaeluk.finance.model.User;
import com.iaeluk.finance.repository.BankRepository;
import com.iaeluk.finance.repository.TransactionRepository;
import com.iaeluk.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    public Bank saveBank(Bank bank, OAuth2User principal) {
        User user = userRepository.findBySub(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Bank newBank = new Bank();
        newBank.setName(bank.getName());
        newBank.setCreditLimit(bank.getCreditLimit());
        newBank.setUser(user);

        return bankRepository.save(newBank);
    }

    public List<Bank> getBanks(@AuthenticationPrincipal OAuth2User principal) {
        String userSub = principal.getName();
        System.out.println("Authenticated user: " + userSub);

        User user = userRepository.findBySub(userSub);
        if (user == null) {
            throw new IllegalStateException("User not found for sub: " + userSub);
        }

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
