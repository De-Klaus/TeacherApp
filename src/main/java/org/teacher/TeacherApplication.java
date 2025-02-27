package org.teacher;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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