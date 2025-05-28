package com.kron.homework.worklog.repository;

import com.kron.homework.worklog.model.WorklogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorklogTypeRepository extends JpaRepository<WorklogType, Long> {
}