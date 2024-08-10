package com.example.banking_system.service;

import com.example.banking_system.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.banking_system.exception.InsufficientFundsException;
import com.example.banking_system.model.Account;
import com.example.banking_system.repository.AccountRepository;

import java.math.BigDecimal;

@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transferMoney(Long toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Client userDetails = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account fromAccount = userDetails.getAccount();
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
