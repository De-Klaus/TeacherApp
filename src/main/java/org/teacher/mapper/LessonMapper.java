package org.teacher.mapper;

import org.mapstruct.*;
import org.teacher.dto.LessonDto;
import org.teacher.entity.Lesson;
import org.teacher.entity.Student;
import org.teacher.entity.Teacher;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LessonMapper {
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "teacher.teacherId", target = "teacherId")
    LessonDto toDto(Lesson lesson);
    @Mapping(target = "lessonId", ignore = true)
    @Mapping(target = "student", source = "studentId", qualifiedByName = "mapLessonIdToStudent")
    @Mapping(target = "teacher", source = "teacherId", qualifiedByName = "mapLessonIdToTeacher")
    Lesson toEntity(LessonDto lessonDto);

    @Named("mapLessonIdToStudent")
    default Student mapLessonIdToStudent(Long studentId) {
        if (studentId == null) return null;
        return Student.builder()
                .studentId(studentId)
                .build();
    }

    @Named("mapLessonIdToTeacher")
    default Teacher mapLessonIdToTeacher(Long teacherId) {
        if (teacherId == null) return null;
        return Teacher.builder()
                .teacherId(teacherId)
                .build();
    }
}
