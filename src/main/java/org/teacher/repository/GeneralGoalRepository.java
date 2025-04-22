package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.teacher.model.GeneralGoal;

@RepositoryRestResource
public interface GeneralGoalRepository extends JpaRepository<GeneralGoal, Long> {
}

