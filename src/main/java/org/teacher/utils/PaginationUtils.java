package org.teacher.utils;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public class PaginationUtils {

    public static HttpHeaders createContentRangeHeader(Page<?> page, String resourceName, int pageNumber, int pageSize) {
        long start = (long) (pageNumber - 1) * pageSize;
        long end = start + page.getNumberOfElements() - 1;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", String.format("%s %d-%d/%d", resourceName, start, end, page.getTotalElements()));
        headers.add("Access-Control-Expose-Headers", "Content-Range");
        return headers;
    }
}
