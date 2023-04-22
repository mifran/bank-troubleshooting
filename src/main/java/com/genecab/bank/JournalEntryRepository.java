package com.genecab.bank;

import org.springframework.data.repository.CrudRepository;

public interface JournalEntryRepository extends CrudRepository<JournalEntry, Long> {
}
