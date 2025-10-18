package org.teacher.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(columnDefinition = "jsonb")
    private String sceneJson;  // JSON-состояние Excalidraw

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
