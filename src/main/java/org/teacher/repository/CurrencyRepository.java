package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}

