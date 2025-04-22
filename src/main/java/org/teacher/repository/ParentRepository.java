package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.teacher.model.Parent;

@RepositoryRestResource
public interface ParentRepository extends JpaRepository<Parent, Long> {
}

