package com.genecab.bank;

public record JournalEntry(Long id, String description, Long instantAsLong, String amount, String balance) {

}
