package org.teacher.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.model.Teacher;
import org.teacher.repository.TeacherRepository;

@RestController
@RequestMapping("teacher/")
@AllArgsConstructor
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    @GetMapping("/welcome")
    public String welcome(){
        return "This is unprotected page";
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String pageForUser(){
        return "This is page for only users";
    }


    @GetMapping("/admins")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String pageForAdmins(){
        return "This is page for only admins";
    }


    @GetMapping("/all")
    public String pageForAll(){
        return "This is page for all employees";
    }

    @GetMapping("/teacher")
    public String teacher() {

        Teacher teacher = new Teacher("John D");
        teacherRepository.save(teacher);

        return "You are a teacher!";
    }
}
