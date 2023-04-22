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

@RestController
@RequestMapping("/journal-entries")
public class JournalEntryController {

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/{requestedId}")
    public ResponseEntity<String> findById(@PathVariable Long requestedId) throws JsonProcessingException {


        if (requestedId.equals(14L)) {
            Long instantLong = Instant.now().toEpochMilli();
            JournalEntry journalEntry = new JournalEntry(14L, "Deposit", instantLong, "-100", "500");
            JavaTimeModule module = new JavaTimeModule();
            objectMapper.registerModule(module);

            String responseBody = new ObjectMapper().writeValueAsString(journalEntry);
            return ResponseEntity.ok(responseBody);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
