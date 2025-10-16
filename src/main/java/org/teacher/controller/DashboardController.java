package org.teacher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teacher.dto.DashboardStatsDto;
import org.teacher.dto.StudentDto;
import org.teacher.dto.TeacherDto;
import org.teacher.mapper.TeacherMapper;
import org.teacher.service.DashboardService;
import org.teacher.service.TeacherService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

}
