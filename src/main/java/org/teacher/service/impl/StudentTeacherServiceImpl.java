package org.teacher.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teacher.dto.StudentTeacherDto;
import org.teacher.entity.StudentTeacher;
import org.teacher.entity.StudentTeacherStatus;
import org.teacher.mapper.StudentTeacherMapper;
import org.teacher.repository.StudentTeacherRepository;
import org.teacher.service.StudentTeacherService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentTeacherServiceImpl implements StudentTeacherService {

    private final StudentTeacherRepository studentTeacherRepository;
    private final StudentTeacherMapper studentTeacherMapper;

    @Override
    public StudentTeacherDto addStudentTeacher(StudentTeacherDto studentTeacherDto) {
        StudentTeacher studentTeacher = studentTeacherMapper.toEntity(studentTeacherDto);
        StudentTeacher saved = studentTeacherRepository.save(studentTeacher);
        return studentTeacherMapper.toDto(saved);
    }

    @Override
    public Optional<StudentTeacherDto> getById(Long studentTeacherId) {
        return studentTeacherRepository.findById(studentTeacherId)
                .map(studentTeacherMapper::toDto);
    }

    @Override
    public List<StudentTeacherDto> getAll() {
        return studentTeacherRepository.findAll().stream()
                .map(studentTeacherMapper::toDto)
                .toList();
    }

    @Override
    public StudentTeacherDto update(Long studentTeacherId, StudentTeacherDto dto) {
        StudentTeacher studentTeacher = studentTeacherRepository.findById(studentTeacherId)
                .orElseThrow(() -> new EntityNotFoundException("StudentTeacher not found: " + studentTeacherId));

        studentTeacher.setEndDate(dto.endDate());
        studentTeacher.setAgreedRate(dto.agreedRate());
        studentTeacher.setStatus(dto.status());

        StudentTeacher saved = studentTeacherRepository.save(studentTeacher);
        return studentTeacherMapper.toDto(saved);
    }

    @Override
    public void delete(Long studentTeacherId) {
        studentTeacherRepository.deleteById(studentTeacherId);
    }

    @Override
    public Optional<StudentTeacher> findActiveByStudentAndTeacher(Long studentId, Long teacherId) {
        return studentTeacherRepository
                .findByStudent_StudentIdAndTeacher_TeacherId(studentId, teacherId)
                .stream()
                .filter(st -> st.getStatus() == StudentTeacherStatus.ACTIVE)
                .findFirst();
    }

    @Override
    public StudentTeacherDto endStudentTeacher(Long studentTeacherId) {
        StudentTeacher st = studentTeacherRepository.findById(studentTeacherId)
                .orElseThrow(() -> new IllegalArgumentException("StudentTeacher not found: " + studentTeacherId));
        st.setStatus(StudentTeacherStatus.ENDED);
        var saved = studentTeacherRepository.save(st);
        return studentTeacherMapper.toDto(saved);
    }

    @Override
    public Optional<StudentTeacher> findActiveByStudent(Long studentId) {
        return studentTeacherRepository.findByStudent_StudentId(studentId)
                .stream()
                .filter(st -> st.getStatus() == StudentTeacherStatus.ACTIVE)
                .findFirst();
    }


}
