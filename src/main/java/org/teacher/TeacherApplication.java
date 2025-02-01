package org.teacher;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TeacherApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherApplication.class, args);
    }

    @RestController
    public static class TeacherApplicationController {

        @GetMapping("/hello")
        public String hello() {
            return "Hello Teacher!";
        }
    }
}