package org.teacher.mapper.impl;

import org.springframework.stereotype.Component;
import org.teacher.dto.TeacherDto;
import org.teacher.entity.Lesson;
import org.teacher.entity.Teacher;
import org.teacher.entity.User;
import org.teacher.mapper.TeacherMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class TeacherMapperImpl implements TeacherMapper {
    @Override
    public TeacherDto toDto(Teacher teacher) {
        if (teacher == null) return null;

        UUID userId = teacher.getUser() != null ? teacher.getUser().getUserId() : null;
        List<Lesson> lessons = teacher.getLessons() != null ? teacher.getLessons() : Collections.emptyList();
        return new TeacherDto(
                teacher.getTeacherId(),
                userId,
                teacher.getSubject(),
                teacher.getTimeZone(),
                lessons
        );
    }

    @Override
    public Teacher toEntity(TeacherDto teacherDto) {
        if (teacherDto == null) return null;

        User user = teacherDto.userId() != null ? User.builder().userId(teacherDto.userId()).build() : null;
        List<Lesson> lessons = teacherDto.lessons() != null ? teacherDto.lessons() : new ArrayList<>();

        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherDto.teacherId());
        teacher.setUser(user);
        teacher.setSubject(teacherDto.subject());
        teacher.setTimeZone(teacherDto.timeZone());
        teacher.setLessons(lessons);
        return teacher;
    }
}
