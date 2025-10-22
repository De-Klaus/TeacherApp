package org.teacher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teacher.dto.StudentDto;
import org.teacher.dto.request.StudentClaimTokenDto;
import org.teacher.mapper.StudentMapper;
import org.teacher.service.StudentService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;


    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Valid StudentDto studentDto) {
        StudentDto responseDto = studentService.addStudent(studentDto);
        return ResponseEntity
                .created(URI.create("/students/" + responseDto.id()))
                .body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Long studentId) {
        return studentService.getById(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<StudentDto> getTeacherByUserId(@PathVariable("userId") UUID userId) {
        return studentService.getStudentByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable("id") Long studentId, @RequestBody @Valid StudentDto dto) {
        return ResponseEntity.ok(studentService.update(studentId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long studentId) {
        studentService.delete(studentId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping("/{id}/claim-token")
    public ResponseEntity<StudentClaimTokenDto> generateClaimToken(@PathVariable("id") Long studentId) {
        StudentClaimTokenDto claimTokenDto = studentService.generateClaimToken(studentId);
        return ResponseEntity.ok(claimTokenDto);
    }
}
