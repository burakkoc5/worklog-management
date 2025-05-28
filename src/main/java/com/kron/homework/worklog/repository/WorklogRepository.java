package com.kron.homework.worklog.repository;

import com.kron.homework.worklog.model.Worklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorklogRepository extends JpaRepository<Worklog, Long> {
}