package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.GeneralGoal;

@Repository
public interface GeneralGoalRepository extends JpaRepository<GeneralGoal, Long> {
}

