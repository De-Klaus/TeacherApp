package org.teacher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teacher.dto.StudentDto;
import org.teacher.dto.TeacherDto;
import org.teacher.mapper.TeacherMapper;
import org.teacher.service.TeacherService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    @PostMapping
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody @Valid TeacherDto teacherDto) {
        TeacherDto responseDto = teacherService.addTeacher(teacherDto);
        return ResponseEntity
                .created(URI.create("/teachers/" + responseDto.id()))
                .body(responseDto);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<TeacherDto> getTeacherByUserId(@PathVariable("userId") UUID userId) {
        return teacherService.getTeacherByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable("id") Long teacherId) {
        return teacherService.getById(teacherId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentDto>> getStudents(@PathVariable("id") Long teacherId) {
        List<StudentDto> students = teacherService.getStudentsByTeacherId(teacherId);
        return ResponseEntity.ok(students);
    }

    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> updateTeacher(@PathVariable("id") Long teacherId, @RequestBody @Valid TeacherDto dto) {
        return ResponseEntity.ok(teacherService.update(teacherId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable("id") Long teacherId) {
        teacherService.delete(teacherId);
        return ResponseEntity.noContent().build();
    }
}
