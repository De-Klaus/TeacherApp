package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_parent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
}
