package com.genecab.bank;

import java.time.LocalDateTime;

public record JournalEntry(Long id, String description, LocalDateTime dateTime, String amount, String balance) {

}
