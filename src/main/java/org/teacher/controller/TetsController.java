package org.teacher.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // Говорим Spring, что это контроллер, который возвращает JSON
@RequestMapping("/api")  // Общий префикс для всех эндпоинтов
public class TetsController {
    // Принимает JSON и возвращает JSON
    @PostMapping("/greet")
    public Greeting greetUser(@RequestBody User user) {
        return new Greeting("Привет, " + user.getName() + "!");
    }
}

// Класс для входящего JSON
class User {
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// Класс для ответа
class Greeting {
    private String message;

    public Greeting(String message) { this.message = message; }
    public String getMessage() { return message; }
}
