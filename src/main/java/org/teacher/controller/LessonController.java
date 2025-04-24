package org.teacher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.model.Lesson;
import org.teacher.repository.LessonRepository;

@RestController
@RequestMapping("/lessons")
public class LessonController extends BaseController<Lesson, Long> {

    public LessonController(LessonRepository repository) {
        super(repository, "lessons");
    }
}
