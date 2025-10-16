package org.teacher.dto;

public record DashboardStatsDto(
        long totalStudents,
        long totalTeachers,
        long availableTariffs,
        long totalLessons
) {}
