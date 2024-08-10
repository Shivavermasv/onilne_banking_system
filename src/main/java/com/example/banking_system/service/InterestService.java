package com.example.banking_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.banking_system.model.Account;
import com.example.banking_system.repository.AccountRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InterestService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Scheduled(fixedRate = 60000)
    public void applyInterest() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            BigDecimal interest = account.getBalance().multiply(BigDecimal.valueOf(0.05));
            BigDecimal newBalance = account.getBalance().add(interest);
            if (newBalance.compareTo(account.getBalance().multiply(BigDecimal.valueOf(2.07))) <= 0) {
                account.setBalance(newBalance);
            } else {
                account.setBalance(account.getBalance().multiply(BigDecimal.valueOf(2.07)));
            }
            accountRepository.save(account);
        }
    }
}
