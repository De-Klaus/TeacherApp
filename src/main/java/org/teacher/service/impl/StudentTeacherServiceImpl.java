package org.teacher.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teacher.dto.StudentDto;
import org.teacher.dto.StudentTeacherDto;
import org.teacher.dto.request.StudentTeacherSystemRequestDto;
import org.teacher.dto.request.UserSystemRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.*;
import org.teacher.mapper.StudentMapper;
import org.teacher.mapper.StudentTeacherMapper;
import org.teacher.mapper.UserMapper;
import org.teacher.repository.*;
import org.teacher.service.AuthService;
import org.teacher.service.StudentTeacherService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentTeacherServiceImpl implements StudentTeacherService {

    private final AuthService authService;

    private final StudentTeacherRepository studentTeacherRepository;
    private final StudentTeacherMapper studentTeacherMapper;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    private final StudentClaimTokenRepository tokenRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public StudentTeacherDto addStudentTeacher(StudentTeacherDto studentTeacherDto) {
        StudentTeacher studentTeacher = studentTeacherMapper.toEntity(studentTeacherDto);
        StudentTeacher saved = studentTeacherRepository.save(studentTeacher);
        return studentTeacherMapper.toDto(saved);
    }

    @Override
    @Transactional
    public StudentTeacherDto createBySystem(StudentTeacherSystemRequestDto dto) {

        UserResponseDto currentUser = authService.getCurrentUser();

        Teacher teacher = teacherRepository.findById(dto.teacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        var user = userMapper.toUserEntity(dto.student());
        var saveUser = userRepository.save(user);

        Student student = studentMapper.toStudent(dto.student());
        student.setUser(saveUser);
        student.setStatus(StudentStatus.CREATED_BY_SYSTEM);
        Student savedStudent = studentRepository.save(student);

        StudentClaimToken claimToken = StudentClaimToken.builder()
                .token(UUID.randomUUID())
                .user(saveUser)
                .student(savedStudent)
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMonths(6))
                .used(false)
                .createdBy(currentUser.email())
                .build();

        tokenRepository.save(claimToken);

        StudentTeacher studentTeacher = studentTeacherMapper.toEntity(new StudentTeacherDto(
                null,
                savedStudent.getStudentId(),
                teacher.getTeacherId(),
                dto.startDate(),
                dto.endDate(),
                dto.agreedRate(),
                StudentTeacherStatus.ACTIVE
        ));
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
