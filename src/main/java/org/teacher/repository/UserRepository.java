package org.teacher.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.teacher.entity.Role;
import org.teacher.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserId(UUID id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(@NotBlank @Email String email);

    @Query("""
        SELECT u
        FROM User u
        WHERE :role MEMBER OF u.roles
        AND u.userId NOT IN (
            SELECT t.user.userId FROM Teacher t
        )
        """)
    List<User> findTeachersWithoutTeacherEntity(@Param("role") Role role);
}
