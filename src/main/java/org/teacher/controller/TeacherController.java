package org.teacher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.model.Teacher;
import org.teacher.repository.TeacherRepository;

@RestController
@RequestMapping("/teachers")
public class TeacherController extends BaseController<Teacher, Long> {

    public TeacherController(TeacherRepository repository) {
        super(repository, "teachers");
    }
}
