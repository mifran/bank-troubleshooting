package com.genecab.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/journal-entries")
public class JournalEntryController {

    @Autowired
    ObjectMapper objectMapper;

    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private AccountRepository accountRepository;

    public JournalEntryController(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<JournalEntry> findById(@PathVariable Long requestedId, Principal principal) throws JsonProcessingException {
        Optional<JournalEntry> journalEntry = journalEntryRepository.findById(requestedId);
        if (journalEntry.isPresent()) {
            Optional<Account> account = getPresentAccountIfPrincipalHasAccessToJournalEntry(journalEntry.orElse(null), principal);
            if (account.isPresent()) {
                return ResponseEntity.ok(journalEntry.get());
            } else {
                // principal does not have access to this JournalEntry
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Optional<Account> getPresentAccountIfPrincipalHasAccessToJournalEntry(JournalEntry journalEntry, Principal principal) {
        Long accountId = journalEntry.getAccount();
        Optional<Account> account = accountRepository.findById(accountId);
        String principalName = principal.getName();
        String accountOwner = account.map(Account::getOwner).orElse(null);
        boolean b = principalName.trim().equals(accountOwner.trim());
        if (b) {
            return account;
        } else {
            return Optional.empty();
        }
    }

    @PostMapping
//    private ResponseEntity createJournalEntry(@RequestBody JournalEntry newJournalEntryRequest, UriComponentsBuilder ubc, Principal principal) {
//        Optional<Account> account = getPresentAccountIfPrincipalHasAccessToJournalEntry(newJournalEntryRequest, principal);
//        if (!account.isPresent()) {
//            return ResponseEntity.badRequest().build();
//        }
//        BigDecimal balance = new JournalEntryHelper(this.journalEntryRepository)
//                .getBalance(newJournalEntryRequest.getAccount());
//        String amountStr = newJournalEntryRequest.getAmount();
//        BigDecimal newBalance = balance.add(new BigDecimal(amountStr));
//        String newBalanceStr = String.valueOf(newBalance);
//        JournalEntry journalEntryToSave = new JournalEntry(
//                newJournalEntryRequest.getId(),
//                newJournalEntryRequest.getAccount(),
//                newJournalEntryRequest.getDescription(),
//                newJournalEntryRequest.getInstant(),
//                newJournalEntryRequest.getAmount(),
//                newBalanceStr);
//        JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntryToSave);
//        URI locationOfAccount = ubc
//                .path("journal-entries/{id}")
//                .buildAndExpand(savedJournalEntry.getId())
//                .toUri();
//        return ResponseEntity.created(locationOfAccount).build();
//    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> findAll(Pageable pageable) {
        Page<JournalEntry> page = journalEntryRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "id"))
                ));
        return ResponseEntity.ok(page.getContent());
    }
}