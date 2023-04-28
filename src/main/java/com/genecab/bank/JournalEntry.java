package com.genecab.bank;

import org.springframework.data.annotation.Id;

public record JournalEntry(@Id Long id, Long account, String description, Long instant, String amount, String balance) {

}
