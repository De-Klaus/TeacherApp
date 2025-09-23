package org.teacher;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.teacher")
public class TeacherApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherApplication.class, args);
    }
}