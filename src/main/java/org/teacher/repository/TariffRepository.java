package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.Tariff;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
}

