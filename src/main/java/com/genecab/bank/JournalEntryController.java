package com.genecab.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
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
}
