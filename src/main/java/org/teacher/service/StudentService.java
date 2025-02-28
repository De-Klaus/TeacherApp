package org.teacher.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.teacher.model.Student;
import org.teacher.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student student) {
        Student existingStudent = getStudentById(id);
        BeanUtils.copyProperties(student, existingStudent, "id");
        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found");
        }
        studentRepository.deleteById(id);
    }
}
