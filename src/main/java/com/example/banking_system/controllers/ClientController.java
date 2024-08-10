package com.example.banking_system.controllers;

import com.example.banking_system.model.Account;
import lombok.Synchronized;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.banking_system.model.Client;
import com.example.banking_system.service.ClientService;
import com.example.banking_system.service.TransferService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private ClientService clientService;

    @Autowired
    private TransferService transferService;

    @PostMapping("/create")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        // Initialize account with a balance of 5000
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(5000));
        client.setAccount(account);

        // Save the client with encrypted password
        client.setPassword(client.getPassword()); // Ensure you have access to the password encoder bean
        Client createdClient = clientService.createClient(client);
        return ResponseEntity.ok(createdClient);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/test")
    @CrossOrigin(origins = "http://localhost:8091")
    public String test() {
        return "CORS is configured!";
    }

    @GetMapping("/search")
    public ResponseEntity<List<Client>> searchClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) String email) {
        return ResponseEntity.ok(clientService.searchClients(name, phone, dateOfBirth, email));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @RequestParam Long toAccountId,
            @RequestParam BigDecimal amount) {
        transferService.transferMoney(toAccountId, amount);
        return ResponseEntity.ok("Transfer successful");
    }

    @PutMapping("/{clientId}/phones")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Client> addPhone(@PathVariable Long clientId, @RequestBody String phone) {
        Client updatedClient = clientService.addPhone(clientId, phone);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{clientId}/phones/{phone}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Client> deletePhone(@PathVariable Long clientId, @PathVariable String phone) {
        Client updatedClient = clientService.deletePhone(clientId, phone);
        return ResponseEntity.ok(updatedClient);
    }

    @PutMapping("/{clientId}/emails")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Client> addEmail(@PathVariable Long clientId, @RequestBody String email) {
        Client updatedClient = clientService.addEmail(clientId, email);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{clientId}/emails/{email}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Client> deleteEmail(@PathVariable Long clientId, @PathVariable String email) {
        Client updatedClient = clientService.deleteEmail(clientId, email);
        return ResponseEntity.ok(updatedClient);
    }

    @PutMapping("/{clientId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Client> updateClient(@PathVariable Long clientId, @RequestBody Client client) {
        Client updatedClient = clientService.updateClient(clientId, client);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{clientId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteClient(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok("Client deleted successfully");
    }
}
