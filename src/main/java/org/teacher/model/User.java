package org.teacher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING) // Если используешь Enum для ролей
    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String name;

    @JsonIgnore // Скрываем пароль при сериализации в JSON
    @Column(nullable = false)
    private String password;

}
