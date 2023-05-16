package com.genecab.bank;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long>, PagingAndSortingRepository<Account, Long> {
    Account findByIdAndOwner(Long id, String owner);
    Page<Account> findByOwner(String owner, PageRequest amount);

    Optional<Account> findById(Long accountId);
}
