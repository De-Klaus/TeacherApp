package org.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.dto.LessonStatusDto;
import org.teacher.dto.StudentStatusDto;
import org.teacher.entity.LessonStatus;
import org.teacher.entity.StudentStatus;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/student-statuses")
@RequiredArgsConstructor
public class StudentStatusController {

    @GetMapping
    public List<StudentStatusDto> getAllStatuses() {
        return Arrays.stream(StudentStatus.values())
                .map(status -> new StudentStatusDto(status.name(), status.getText(), status.getColor()))
                .toList();
    }
}
