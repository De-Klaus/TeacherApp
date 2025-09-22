package org.teacher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.entity.User;
import org.teacher.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User, Long> {


    public UserController(UserRepository repository) {
        super(repository, "users");
    }

}
