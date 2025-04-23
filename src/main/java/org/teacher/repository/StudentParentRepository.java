package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.teacher.model.StudentParent;

@Repository
public interface StudentParentRepository extends JpaRepository<StudentParent, Long> {
}

