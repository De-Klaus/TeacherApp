package org.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.dto.DashboardStatsDto;
import org.teacher.dto.TimeZoneResponseDto;
import org.teacher.entity.TimeZoneOption;
import org.teacher.service.DashboardService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    @GetMapping("/timezones")
    public List<TimeZoneResponseDto> getStats() {
        return Arrays.stream(TimeZoneOption.values())
                .map(tz -> new TimeZoneResponseDto(
                        tz.getValue(),
                        tz.getValue() + " (" + tz.getCity() + ")"
                ))
                .toList();
    }

}
