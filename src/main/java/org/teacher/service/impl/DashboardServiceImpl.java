package org.teacher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teacher.dto.DashboardStatsDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.Role;
import org.teacher.repository.LessonRepository;
import org.teacher.repository.StudentRepository;
import org.teacher.repository.StudentTeacherRepository;
import org.teacher.repository.TeacherRepository;
import org.teacher.service.AuthService;
import org.teacher.service.DashboardService;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AuthService authService;
    private final StudentRepository studentRepository;
    private final StudentTeacherRepository studentTeacherRepository;
    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;

    @Override
    public DashboardStatsDto getStats() {
        UserResponseDto currentUser = authService.getCurrentUser();

        if (currentUser.hasRole(Role.TEACHER)) {
            long totalStudents = studentTeacherRepository.countActiveStudentsByTeacherUserId(currentUser.userId());
            long totalLessons = lessonRepository.countByTeacher_User_UserId(currentUser.userId());
            return new DashboardStatsDto(
                    totalStudents,
                    1L,
                    0L,
                    totalLessons
            );
        }
        if (currentUser.hasRole(Role.ADMIN)) {
            long totalStudents = studentRepository.count();
            long totalTeachers = teacherRepository.count();
            long totalLessons = lessonRepository.count();
            return new DashboardStatsDto(
                    totalStudents,
                    totalTeachers,
                    0L,
                    totalLessons
            );
        }
        return null;
    }
}
