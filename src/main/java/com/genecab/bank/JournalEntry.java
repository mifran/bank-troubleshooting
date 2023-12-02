package com.genecab.bank;

import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;

@Entity
public class JournalEntry {
    // @Id Long id, Long account, String description, Long instant, String amount, String balance

    @Id
    private Long id;
    private Long account;
    private String description;
    private Long instant;
    private String amount;
    private String balance;

    public JournalEntry() {
    }

    public JournalEntry(Long id, Long account, String description, Long instant, String amount, String balance) {
        this.id = id;
        this.account = account;
        this.description = description;
        this.instant = instant;
        this.amount = amount;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getInstant() {
        return instant;
    }

    public void setInstant(Long instant) {
        this.instant = instant;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
