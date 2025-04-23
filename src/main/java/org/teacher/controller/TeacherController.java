package org.teacher.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.teacher.dto.TeacherDTO;
import org.teacher.model.Teacher;
import org.teacher.repository.TeacherRepository;
import org.teacher.repository.UserRepository;

@RestController
@RequestMapping("/teachers")
public class TeacherController extends BaseController<Teacher, Long> {

    UserRepository userRepository;

    public TeacherController(TeacherRepository repository, UserRepository userRepository) {
        super(repository, "teachers");
        this.userRepository = userRepository;
    }

    /*@PostMapping
    public Teacher create(@RequestBody TeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setName(dto.name);
        teacher.setUser(userRepository.findById(dto.userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
        return super.create(teacher);
    }*/

}
