package org.teacher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.entity.User;
import org.teacher.repository.UserRepository;

@RestController
@RequestMapping("/teachers")
public class TeacherController extends BaseController<User, Long> {


    public TeacherController(UserRepository repository) {
        super(repository, "users");
    }

}
