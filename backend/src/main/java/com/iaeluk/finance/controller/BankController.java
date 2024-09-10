package com.iaeluk.finance.controller;

import com.iaeluk.finance.model.Bank;
import com.iaeluk.finance.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/banks")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping()
    public ResponseEntity<Bank> saveBank(@RequestBody Bank bank) {
        Bank newBank =  bankService.saveBank(bank);
        return ResponseEntity.ok(newBank);
    }

    @GetMapping("/list")
    public List<Bank> getBanks() {
        return bankService.getBanks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bank> getBankById(@PathVariable Long id) {
        Optional<Bank> Bank = bankService.findById(id);
        return Bank.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bank> updateBank(@PathVariable Long id, @RequestBody Bank bank) {
        Bank updatedBank = bankService.updateBank(id, bank);
        return ResponseEntity.ok(updatedBank);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBank(@PathVariable Long id) {
        if (bankService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Bank not found"));
        }

        bankService.deleteBank(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Bank deleted successfully"));
    }

}
