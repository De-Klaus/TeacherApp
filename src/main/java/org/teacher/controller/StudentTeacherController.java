package org.teacher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teacher.dto.StudentTeacherDto;
import org.teacher.mapper.StudentTeacherMapper;
import org.teacher.service.StudentTeacherService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/student-teachers")
@RequiredArgsConstructor
public class StudentTeacherController {

    private final StudentTeacherService studentTeacherService;
    private final StudentTeacherMapper studentTeacherMapper;

    @PostMapping
    public ResponseEntity<StudentTeacherDto> createStudentTeacher(@RequestBody @Valid StudentTeacherDto studentTeacherDto) {
        StudentTeacherDto responseDto = studentTeacherService.addStudentTeacher(studentTeacherDto);
        return ResponseEntity
                .created(URI.create("/student-teachers/" + responseDto.id()))
                .body(responseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<StudentTeacherDto> getStudentTeacherById(@PathVariable("id") Long studentTeacherId) {
        return studentTeacherService.getById(studentTeacherId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<StudentTeacherDto>> getAllStudentTeachers() {
        return ResponseEntity.ok(studentTeacherService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentTeacherDto> updateStudentTeacher(@PathVariable("id") Long studentTeacherId, @RequestBody @Valid StudentTeacherDto dto) {
        return ResponseEntity.ok(studentTeacherService.update(studentTeacherId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentTeacher(@PathVariable("id") Long studentTeacherId) {
        studentTeacherService.delete(studentTeacherId);
        return ResponseEntity.noContent().build();
    }

    /**
     * End an existing StudentTeacher relationship.
     * Changes status to ENDED.
     */
    @PatchMapping("/{id}/end")
    public ResponseEntity<StudentTeacherDto> endStudentTeacher(
            @PathVariable("id") Long studentTeacherId
    ) {
        StudentTeacherDto updated = studentTeacherService.endStudentTeacher(studentTeacherId);
        return ResponseEntity.ok(updated);
    }
}
