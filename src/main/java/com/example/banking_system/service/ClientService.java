package com.example.banking_system.service;

import com.example.banking_system.model.Account;
import com.example.banking_system.model.Client;
import com.example.banking_system.repository.AccountRepository;
import com.example.banking_system.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Client> getUser() {
        return clientRepository.findAll();
    }

    public Client createClient(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        Account account = client.getAccount();
        if (account != null) {
            account.setBalance(new BigDecimal("5000")); // Ensure balance is set to 5000
            accountRepository.save(account);
        }
        return clientRepository.save(client);
    }

    private static <T> List<T> optionalToList(Optional<T> optional) {
        return optional.map(Collections::singletonList).orElse(Collections.emptyList());
    }

    public List<Client> searchClients(String name, String phone, String email) {
        if (name != null) {
            return optionalToList(clientRepository.findByNameContaining(name));
        } else if (phone != null) {
            return clientRepository.findByPhone(phone);
        } else if (email != null) {
            return clientRepository.findByEmail(email);
        }
        return null;
    }

    @Transactional
    public Client addPhone(Long clientId, String phone) {
        return updateClientDetail(clientId, "phone", phone);
    }

    @Transactional
    public Client deletePhone(Long clientId, String phone) {
        return updateClientDetail(clientId, "phone", null, phone);
    }

    @Transactional
    public Client addEmail(Long clientId, String email) {
        return updateClientDetail(clientId, "email", email);
    }

    @Transactional
    public Client deleteEmail(Long clientId, String email) {
        return updateClientDetail(clientId, "email", null, email);
    }

    @Transactional
    public Client updateClient(Long clientId, Client client) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isPresent()) {
            Client existingClient = optionalClient.get();
            existingClient.setName(client.getName());
            existingClient.setDateOfBirth(client.getDateOfBirth());
            existingClient.setPhone(client.getPhone());
            existingClient.setEmail(client.getEmail());
            return clientRepository.save(existingClient);
        } else {
            throw new RuntimeException("Client not found");
        }
    }

    @Transactional
    public void deleteClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Client not found");
        }
        clientRepository.deleteById(clientId);
    }

    @Transactional
    public void transferMoney(Long fromClientId, Long toClientId, BigDecimal amount) {
        Optional<Client> fromClientOpt = clientRepository.findById(fromClientId);
        Optional<Client> toClientOpt = clientRepository.findById(toClientId);

        if (fromClientOpt.isPresent() && toClientOpt.isPresent()) {
            Client fromClient = fromClientOpt.get();
            Client toClient = toClientOpt.get();

            Account fromAccount = fromClient.getAccount();
            Account toAccount = toClient.getAccount();

            if (fromAccount != null && toAccount != null && fromAccount.getBalance().compareTo(amount) >= 0) {
                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                toAccount.setBalance(toAccount.getBalance().add(amount));

                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);
            } else {
                throw new RuntimeException("Insufficient funds or account not found");
            }
        } else {
            throw new RuntimeException("Client not found");
        }
    }

    private Client updateClientDetail(Long clientId, String field, String newValue) {
        return updateClientDetail(clientId, field, newValue, null);
    }

    private Client updateClientDetail(Long clientId, String field, String newValue, String oldValue) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            if (field.equals("phone")) {
                if (oldValue == null || oldValue.equals(client.getPhone())) {
                    client.setPhone(newValue);
                } else {
                    throw new RuntimeException("Phone number does not match");
                }
            } else if (field.equals("email")) {
                if (oldValue == null || oldValue.equals(client.getEmail())) {
                    client.setEmail(newValue);
                } else {
                    throw new RuntimeException("Email does not match");
                }
            }
            return clientRepository.save(client);
        } else {
            throw new RuntimeException("Client not found");
        }
    }
}
