package com.genecab.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.net.URI;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.security.Principal;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private AccountRepository accountRepository;
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @GetMapping("/{requestedId}")
    public ResponseEntity<Account> findById(@PathVariable Long requestedId, Principal principal) throws JsonProcessingException {
        Optional<Account> accountOptional = Optional.ofNullable(findAccount(requestedId, principal));

        if (accountOptional.isPresent()) {
            return ResponseEntity.ok(accountOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity createAccount(
            @RequestBody Account newAccountRequest,
            UriComponentsBuilder ubc,
            Principal principal) {
        Account accountWithOwner = new Account(newAccountRequest.getId(),
                newAccountRequest.getName(),
                newAccountRequest.getType(),
                principal.getName());
        Account savedCashCard = accountRepository.save(accountWithOwner);
        URI locationOfAccount = ubc
                .path("accounts/{id}")
                .buildAndExpand(savedCashCard.getId())
                .toUri();
        return ResponseEntity.created(locationOfAccount).build();
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAll(Pageable pageable, Principal principal) {
        Page<Account> page = accountRepository.findByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                ));
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody Account accountUpdate, Principal principal) {
        Account account = findAccount(requestedId, principal);
        if (account != null) {
            Account updatedAccount = new Account(account.getId(), accountUpdate.getName(), accountUpdate.getType(), principal.getName());
            accountRepository.save(updatedAccount);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private Account findAccount(Long requestedId, Principal principal) {
        return accountRepository.findByIdAndOwner(requestedId, principal.getName());
    }
}
