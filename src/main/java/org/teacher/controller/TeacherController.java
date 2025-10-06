package org.teacher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teacher.dto.TeacherDto;
import org.teacher.mapper.TeacherMapper;
import org.teacher.service.TeacherService;

import java.net.URI;
import java.util.List;

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
                .created(URI.create("/teachers/" + responseDto.teacherId()))
                .body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable Long teacherId) {
        return teacherService.getById(teacherId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> updateTeacher(@PathVariable Long teacherId, @RequestBody @Valid TeacherDto dto) {
        return ResponseEntity.ok(teacherService.update(teacherId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long teacherId) {
        teacherService.delete(teacherId);
        return ResponseEntity.noContent().build();
    }
}
