package org.teacher.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teacher.dto.StudentDto;
import org.teacher.dto.TeacherDto;
import org.teacher.entity.Role;
import org.teacher.entity.Teacher;
import org.teacher.entity.User;
import org.teacher.mapper.StudentMapper;
import org.teacher.mapper.TeacherMapper;
import org.teacher.repository.StudentTeacherRepository;
import org.teacher.repository.TeacherRepository;
import org.teacher.repository.UserRepository;
import org.teacher.service.TeacherService;
import org.teacher.entity.StudentTeacher;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final StudentTeacherRepository studentTeacherRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final TeacherMapper teacherMapper;
    private final StudentMapper studentMapper;

    @Override
    public TeacherDto addTeacher(TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        User user = userRepository.findById(teacherDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + teacherDto.userId()));
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(Role.TEACHER);
        var saveUser = userRepository.save(user);
        teacher.setUser(saveUser);
        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toDto(saved);
    }

    @Override
    public Optional<TeacherDto> getById(Long teacherId) {
        return teacherRepository.findById(teacherId)
                .map(teacherMapper::toDto);
    }

    @Override
    public Optional<TeacherDto> getTeacherByUserId(UUID userId) {
        return teacherRepository.findByUser_UserId(userId)
                .map(teacherMapper::toDto);
    }

    @Override
    public Optional<TeacherDto> findByUserId(UUID userId) {
        return teacherRepository.findByUser_UserId(userId)
                .map(teacherMapper::toDto);
    }

    @Override
    public List<TeacherDto> getAll() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toDto)
                .toList();
    }

    @Override
    public TeacherDto update(Long teacherId, TeacherDto dto) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + teacherId));

        teacher.setSubject(dto.subject());
        teacher.setTimeZone(dto.timeZone());

        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toDto(saved);
    }

    @Override
    public void delete(Long teacherId) {
        teacherRepository.deleteById(teacherId);
    }

    @Override
    public List<StudentDto> getStudentsByTeacherId(Long teacherId) {
        List<StudentTeacher> relations = studentTeacherRepository.findByTeacher_TeacherId(teacherId);
        return relations.stream()
                .map(StudentTeacher::getStudent)
                .map(studentMapper::toDto)
                .toList();
    }


}
