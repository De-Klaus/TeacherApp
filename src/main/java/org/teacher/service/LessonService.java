package org.teacher.service;

import lombok.*;
import org.springframework.stereotype.*;
import org.teacher.model.Lesson;
import org.teacher.model.User;
import org.teacher.repository.LessonRepository;
import org.teacher.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    public Lesson addLesson(Long userId, Lesson lesson) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        lesson.setUser(user);
        return lessonRepository.save(lesson);
    }

    public List<Object[]> getSummary(Long userId) {
        return lessonRepository.getLessonSummary(userId);
    }
}
