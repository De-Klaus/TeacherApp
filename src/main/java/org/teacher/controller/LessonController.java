package org.teacher.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.dto.LessonDto;
import org.teacher.model.Lesson;
import org.teacher.model.Student;
import org.teacher.repository.LessonRepository;
import org.teacher.repository.StudentRepository;

@RestController
@RequestMapping("/lessons")
public class LessonController extends BaseController<Lesson, Long> {

    StudentRepository studentRepository;

    public LessonController(LessonRepository repository) {
        super(repository, "lessons");
    }

}
