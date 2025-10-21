package org.teacher.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teacher.dto.StudentDto;
import org.teacher.dto.request.StudentClaimTokenDto;
import org.teacher.entity.*;
import org.teacher.mapper.StudentMapper;
import org.teacher.repository.LessonRepository;
import org.teacher.repository.PaymentRepository;
import org.teacher.repository.StudentClaimTokenRepository;
import org.teacher.repository.StudentRepository;
import org.teacher.service.StudentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PaymentRepository paymentRepository;
    private final LessonRepository lessonRepository;
    private final StudentMapper studentMapper;
    private final StudentClaimTokenRepository tokenRepository;

    @Override
    public StudentDto addStudent(StudentDto studentDto) {
        Student student = studentMapper.toEntity(studentDto);
        Student saved = studentRepository.save(student);
        return studentMapper.toDto(saved);
    }

    @Override
    public Optional<StudentDto> getById(Long studentId) {
        return studentRepository.findById(studentId)
                .map(studentMapper::toDto);
    }

    @Override
    public Optional<StudentDto> getStudentByUserId(UUID userId) {
        return studentRepository.findByUser_UserId(userId)
                .map(studentMapper::toDto);
    }

    @Override
    public Optional<StudentDto> findByUserId(UUID userId) {
        return studentRepository.findByUser_UserId(userId)
                .map(studentMapper::toDto);
    }

    @Override
    public List<StudentDto> getAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    public StudentDto update(Long studentId, StudentDto dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + studentId));

        student.setBirthDate(dto.birthDate());
        student.setPhoneNumber(dto.phoneNumber());
        student.setCity(dto.city());
        student.setTimeZone(dto.timeZone());
        student.setGrade(dto.grade());
        student.setSchool(dto.school());

        Student saved = studentRepository.save(student);
        return studentMapper.toDto(saved);
    }

    @Override
    public void delete(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    /**
     * Calculates the balance for a student:
     * balance = sum(payments) - sum(completed lessons prices).
     *
     * @param studentId UUID of the student
     * @return BigDecimal current balance
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateBalance(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        // 1. Сумма платежей
        BigDecimal totalPayments = paymentRepository.findByStudent(student).stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Сумма завершённых уроков
        BigDecimal totalLessonsCost = lessonRepository.findByStudentAndStatus(student, LessonStatus.COMPLETED).stream()
                .map(Lesson::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Баланс = платежи – завершённые уроки
        return totalPayments.subtract(totalLessonsCost);
    }

    @Override
    @Transactional
    public StudentClaimTokenDto generateClaimToken(Long studentId, String createdBy) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        if (student.getStatus() != StudentStatus.CREATED_BY_SYSTEM) {
            throw new IllegalStateException("Claim token can only be generated for students with status CREATED_BY_SYSTEM");
        }

        LocalDateTime now = LocalDateTime.now();
        StudentClaimToken existingToken = tokenRepository.findFirstByStudentAndUsedFalseAndExpiresAtAfter(student, now)
                .orElse(null);

        StudentClaimToken token;
        if (existingToken != null) {
            token = existingToken;
        } else {
            token = StudentClaimToken.builder()
                    .token(UUID.randomUUID())
                    .student(student)
                    .createdAt(now)
                    .expiresAt(now.plusMonths(6))
                    .used(false)
                    .createdBy(createdBy)
                    .build();
            tokenRepository.save(token);
        }

        return new StudentClaimTokenDto(student.getStudentId(), token.getToken(), token.getExpiresAt());
    }


}
