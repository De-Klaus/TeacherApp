package org.teacher.mapper.impl;

import org.springframework.stereotype.Component;
import org.teacher.dto.StudentDto;
import org.teacher.entity.Student;
import org.teacher.entity.User;
import org.teacher.mapper.StudentMapper;

import java.util.UUID;

@Component
public class StudentMapperImpl implements StudentMapper {


    @Override
    public StudentDto toDto(Student student) {
        if (student == null) return null;
        UUID userId = student.getUser() != null ? student.getUser().getUserId() : null;
        String firstName = student.getUser() != null ? student.getUser().getFirstName() : null;
        String lastName = student.getUser() != null ? student.getUser().getLastName() : null;
        return new StudentDto(
                            student.getStudentId(),
                            userId,
                            firstName,
                            lastName,
                            student.getBirthDate(),
                            student.getPhoneNumber(),
                            student.getCity(),
                            student.getTimeZone(),
                            student.getGrade(),
                            student.getSchool()
        );
    }

    @Override
    public Student toEntity(StudentDto studentDto) {
        if (studentDto == null) return null;
        User user = studentDto.userId() != null ? User.builder().userId(studentDto.userId()).build() : null;

        Student student = new Student();
        student.setStudentId(studentDto.id());
        student.setUser(user);
        student.setBirthDate(studentDto.birthDate());
        student.setPhoneNumber(studentDto.phoneNumber());
        student.setCity(studentDto.city());
        student.setTimeZone(studentDto.timeZone());
        student.setGrade(studentDto.grade());
        student.setSchool(studentDto.school());
        return student;
    }
}
