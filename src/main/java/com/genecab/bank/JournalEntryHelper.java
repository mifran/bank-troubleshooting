package com.genecab.bank;

import java.math.BigDecimal;
import java.util.List;

public class JournalEntryHelper {

    private JournalEntryRepository journalEntryRepository;


    public JournalEntryHelper(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }
    BigDecimal getBalance(Long account) {
        List<String> ls = journalEntryRepository.getBalanceList(account);
        String balanceStr = ls.get(0).trim();
        return new BigDecimal(balanceStr);
    }

}
