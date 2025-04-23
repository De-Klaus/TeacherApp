package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}

