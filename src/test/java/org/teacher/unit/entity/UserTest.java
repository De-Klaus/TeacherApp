package org.teacher.unit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.teacher.entity.Role;
import org.teacher.entity.User;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .userId(UUID.randomUUID())
                .email("user1@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(EnumSet.of(Role.STUDENT))
                .build();

        user2 = User.builder()
                .userId(user1.getUserId())
                .email("user1@example.com")
                .firstName("John")
                .lastName("Doe")
                .roles(EnumSet.of(Role.STUDENT))
                .build();
    }

    @Test
    void testEqualsAndHashCode() {
        // Проверяем, что два объекта с одинаковым userId и email равны
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());

        // Меняем email → объекты не равны
        user2.setEmail("other@example.com");
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void shouldSetAndGetRolesCorrectly() {
        // Создаём пользователя
        User user = new User();
        user.setUserId(java.util.UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPassword("password");

        // Назначаем роли
        Set<Role> roles = new HashSet<>();
        roles.add(Role.STUDENT);
        roles.add(Role.TEACHER);
        user.setRoles(roles);

        // Проверяем, что роли установились корректно
        assertThat(user.getRoles()).isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(Role.STUDENT, Role.TEACHER);

        // Проверяем добавление новой роли
        user.getRoles().add(Role.ADMIN);
        assertThat(user.getRoles()).hasSize(3)
                .contains(Role.ADMIN);
    }

    @Test
    void shouldHandleEmptyRoles() {
        User user = new User();
        user.setRoles(new HashSet<>());

        assertThat(user.getRoles()).isEmpty();
    }

}