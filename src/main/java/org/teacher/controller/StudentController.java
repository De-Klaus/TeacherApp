package org.teacher.controller;

import org.springframework.web.bind.annotation.*;
import org.teacher.model.Student;
import org.teacher.repository.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController extends BaseController<Student, Long> {

    public StudentController(StudentRepository repository) {
        super(repository, "students");
    }

}
