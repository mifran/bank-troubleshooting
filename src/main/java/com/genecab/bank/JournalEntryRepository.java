package com.genecab.bank;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JournalEntryRepository extends CrudRepository<JournalEntry, Long>, PagingAndSortingRepository<JournalEntry, Long> {

    @Query(nativeQuery = true,
           value = "SELECT balance FROM journal_entry je where id= (SELECT max(id) FROM journal_entry WHERE account = :account)")
    List<String> getBalanceList(@Param("account") Long account);

}