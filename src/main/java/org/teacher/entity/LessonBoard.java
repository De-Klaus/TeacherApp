package org.teacher.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "lesson_boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_board_id")
    private Long lessonBoardId;

    @OneToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String sceneJson;  // JSON-состояние Excalidraw

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonBoard lessonBoard = (LessonBoard) o;
        return Objects.equals(lessonBoardId, lessonBoard.lessonBoardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonBoardId);
    }
}
