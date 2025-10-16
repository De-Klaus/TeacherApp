package org.teacher.mapper.impl;

import org.springframework.stereotype.Component;
import org.teacher.dto.LessonStatusDto;
import org.teacher.entity.LessonStatus;
import org.teacher.mapper.LessonStatusMapper;

@Component
public class LessonStatusMapperImpl implements LessonStatusMapper {
    @Override
    public LessonStatusDto toDto(LessonStatus status) {
        return new LessonStatusDto(status.name(), status.getText(), status.getColor());
    }

    @Override
    public LessonStatus toEntity(LessonStatusDto lsDto) {
        if (lsDto == null || lsDto.name() == null) return null;

        try {
            return LessonStatus.valueOf(lsDto.name());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown lesson status: " + lsDto.name(), e);
        }
    }
}
