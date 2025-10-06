package org.teacher.mapper;

import org.mapstruct.*;
import org.teacher.dto.StudentTeacherDto;
import org.teacher.entity.Student;
import org.teacher.entity.StudentTeacher;
import org.teacher.entity.Teacher;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentTeacherMapper {
    @Mapping(source = "student.studentId", target = "studentId")
    @Mapping(source = "teacher.teacherId", target = "teacherId")
    StudentTeacherDto toDto(StudentTeacher studentTeacher);
    @Mapping(target = "studentTeacherId", ignore = true)
    @Mapping(target = "student", source = "studentId", qualifiedByName = "mapStudentIdToStudent")
    @Mapping(target = "teacher", source = "teacherId", qualifiedByName = "mapTeacherIdToTeacher")
    StudentTeacher toEntity(StudentTeacherDto studentTeacherDto);

    @Named("mapStudentIdToStudent")
    default Student mapStudentIdToStudent(Long studentId) {
        if (studentId == null) return null;
        return Student.builder()
                .studentId(studentId)
                .build();
    }

    @Named("mapTeacherIdToTeacher")
    default Teacher mapTeacherIdToTeacher(Long teacherId) {
        if (teacherId == null) return null;
        return Teacher.builder()
                .teacherId(teacherId)
                .build();
    }
}
