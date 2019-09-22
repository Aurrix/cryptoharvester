package com.finplant.cryptoharvester.cryptoharvester.repositories;

import com.finplant.cryptoharvester.cryptoharvester.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {
}
