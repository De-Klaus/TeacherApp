package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.teacher.model.Tariff;
import org.teacher.model.User;

import java.util.List;

@RepositoryRestResource
public interface TariffRepository extends JpaRepository<Tariff, Long> {
    List<Tariff> findByTeacherId(User teacher);
}

