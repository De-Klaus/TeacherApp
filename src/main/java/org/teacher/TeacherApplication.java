package org.teacher;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.model.Teacher;
import org.teacher.repository.TeacherRepository;

@SpringBootApplication
@EntityScan("org.teacher.model")
@EnableJpaRepositories("org.teacher.repository")
public class TeacherApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherApplication.class, args);
    }

    /*@RestController
    public static class TeacherApplicationController {

        @Autowired
        private TeacherRepository teacherRepository;

        @GetMapping("/teacher")
        public String teacher() {

            Teacher teacher = new Teacher("John D");
            teacherRepository.save(teacher);

            return "You are a teacher!";
        }

        @GetMapping("/")
        public String hello() {
            return "Hello Teacher!";
        }
    }*/
}