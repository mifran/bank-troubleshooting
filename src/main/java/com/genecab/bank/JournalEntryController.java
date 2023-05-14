package com.genecab.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/journal-entries")
public class JournalEntryController {

    @Autowired
    ObjectMapper objectMapper;

    private JournalEntryRepository journalEntryRepository;

    public JournalEntryController(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<JournalEntry> findById(@PathVariable Long requestedId) throws JsonProcessingException {
        Optional<JournalEntry> journalEntry = journalEntryRepository.findById(requestedId);

        if (journalEntry.isPresent()) {
            return ResponseEntity.ok(journalEntry.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity createAccount(@RequestBody JournalEntry newJournalEntryRequest, UriComponentsBuilder ubc) {
        String balanceStr = journalEntryRepository
                .getBalance(newJournalEntryRequest.account())
                .get(0)
                .trim();
        String amountStr = newJournalEntryRequest.amount();
        BigDecimal newBalance = new BigDecimal(balanceStr).add(new BigDecimal(amountStr));
        String newBalanceStr = String.valueOf(newBalance);
        JournalEntry journalEntryToSave = new JournalEntry(
                newJournalEntryRequest.id(),
                newJournalEntryRequest.account(),
                newJournalEntryRequest.description(),
                newJournalEntryRequest.instant(),
                newJournalEntryRequest.amount(),
                newBalanceStr);
        JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntryToSave);
        URI locationOfAccount = ubc
                .path("journal-entries/{id}")
                .buildAndExpand(savedJournalEntry.id())
                .toUri();
        return ResponseEntity.created(locationOfAccount).build();
    }

    @GetMapping()
    public ResponseEntity<Iterable<JournalEntry>> findAll() {
        return ResponseEntity.ok(journalEntryRepository.findAll());
    }
}
