package org.teacher.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teacher.dto.TeacherDto;
import org.teacher.entity.Teacher;
import org.teacher.mapper.TeacherMapper;
import org.teacher.repository.TeacherRepository;
import org.teacher.service.TeacherService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Override
    public TeacherDto addTeacher(TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toDto(saved);
    }

    @Override
    public Optional<TeacherDto> getById(Long teacherId) {
        return teacherRepository.findById(teacherId)
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


}
