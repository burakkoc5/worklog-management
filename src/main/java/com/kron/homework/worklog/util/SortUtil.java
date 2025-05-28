package com.kron.homework.worklog.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SortUtil {

    public static Pageable validateAndGetPageable(Pageable pageable, List<String> validSortProperties) {
        if (pageable.getSort().isSorted()) {
            String property = pageable.getSort().iterator().next().getProperty();
            if (!isValidSortProperty(property, validSortProperties)) {
                log.warn("Invalid sort property: {}", property);
                return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id"));
            }
        }
        return pageable;
    }

    private static boolean isValidSortProperty(String property, List<String> validSortProperties) {
        return validSortProperties.contains(property);
    }
}