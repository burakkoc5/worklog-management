package com.kron.homework.worklog.service;

import com.kron.homework.worklog.util.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Function;

@Slf4j
public abstract class BaseService<T, D> {

    protected abstract JpaRepository<T, Long> getRepository();
    protected abstract Function<T, D> getMapper();
    protected abstract List<String> getValidSortProperties();

    public Page<D> getAllWithPagination(Pageable pageable) {
        log.debug("Fetching all entities with pageable: {}", pageable);
        Pageable validatedPageable = SortUtil.validateAndGetPageable(pageable, getValidSortProperties());
        return getRepository().findAll(validatedPageable).map(getMapper());
    }
}