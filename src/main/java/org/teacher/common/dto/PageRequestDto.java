package org.teacher.common.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

public record PageRequestDto(
        int page,
        int size,
        String[] sort
) {

    public PageRequestDto() {
        this(0, 20, new String[0]);
    }

    public Pageable toPageable() {
        if (sort == null || sort.length == 0) {
            return PageRequest.of(page, size);
        }
        List<Sort.Order> orders = Arrays.stream(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(
                            parts.length > 1 ? Sort.Direction.fromString(parts[1]) : Sort.Direction.ASC,
                            parts[0]
                    );
                })
                .toList();
        return PageRequest.of(page, size, Sort.by(orders));
    }
}