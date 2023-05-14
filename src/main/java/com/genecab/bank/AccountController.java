package com.genecab.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import java.net.URI;
import org.springframework.web.bind.annotation.RequestBody;
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
        Optional<Account> accountOptional = Optional.ofNullable(accountRepository.findByIdAndOwner(requestedId, principal.getName()));

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
        Account accountWithOwner = new Account(null,
                newAccountRequest.name(),
                newAccountRequest.type(),
                principal.getName());
        Account savedCashCard = accountRepository.save(accountWithOwner);
        URI locationOfAccount = ubc
                .path("accounts/{id}")
                .buildAndExpand(savedCashCard.id())
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
}
