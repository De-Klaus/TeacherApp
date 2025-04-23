package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
}

