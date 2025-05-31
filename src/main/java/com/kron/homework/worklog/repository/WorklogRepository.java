package com.kron.homework.worklog.repository;

import com.kron.homework.worklog.model.Worklog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorklogRepository extends JpaRepository<Worklog, Long> {
    Page<Worklog> findByEmployeeId(Long employeeId, Pageable pageable);
}