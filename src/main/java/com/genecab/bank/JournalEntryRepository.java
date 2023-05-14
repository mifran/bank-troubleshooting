package com.genecab.bank;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface JournalEntryRepository extends CrudRepository<JournalEntry, Long>, PagingAndSortingRepository<JournalEntry, Long> {

    @Query("SELECT BALANCE FROM JOURNAL_ENTRY je where id= (SELECT max(id) FROM JOURNAL_ENTRY WHERE ACCOUNT = 1001)")
    List<String> getBalance(Long account);
}
