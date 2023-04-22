package com.genecab.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private AccountRepository accountRepository;
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @GetMapping("/{requestedId}")
    public ResponseEntity<Account> findById(@PathVariable Long requestedId) throws JsonProcessingException {
        Optional<Account> accountOptional = accountRepository.findById(1001L);

        if (accountOptional.isPresent()) {
            return ResponseEntity.ok(accountOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
