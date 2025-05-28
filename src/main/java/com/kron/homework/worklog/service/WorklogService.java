package com.kron.homework.worklog.service;

import com.kron.homework.worklog.dto.worklog.CreateWorklogDto;
import com.kron.homework.worklog.dto.worklog.UpdateWorklogDto;
import com.kron.homework.worklog.dto.worklog.WorklogResponseDto;
import com.kron.homework.worklog.exception.EntityNotFoundException;
import com.kron.homework.worklog.model.Worklog;
import com.kron.homework.worklog.repository.WorklogRepository;
import com.kron.homework.worklog.mapper.WorklogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class WorklogService extends BaseService<Worklog, WorklogResponseDto> {

    private final WorklogRepository worklogRepository;
    private final WorklogMapper worklogMapper;

    @Autowired
    public WorklogService(WorklogRepository worklogRepository, WorklogMapper worklogMapper) {
        this.worklogRepository = worklogRepository;
        this.worklogMapper = worklogMapper;
        log.info("WorklogService initialized");
    }

    @Override
    protected JpaRepository<Worklog, Long> getRepository() {
        return worklogRepository;
    }

    @Override
    protected Function<Worklog, WorklogResponseDto> getMapper() {
        return worklogMapper::toResponseDto;
    }

    @Override
    protected List<String> getValidSortProperties() {
        return Arrays.asList("id", "monthDate", "effort");
    }

    public Page<WorklogResponseDto> getAllWorklogs(Pageable pageable) {
        return getAllWithPagination(pageable);
    }

    public WorklogResponseDto getWorklogById(Long id) {
        log.debug("Fetching worklog by id: {}", id);
        return worklogRepository.findById(id)
                .map(worklogMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Worklog", id));
    }

    public WorklogResponseDto createWorklog(CreateWorklogDto createWorklogDto) {
        log.debug("Creating new worklog: {}", createWorklogDto);
        Worklog worklog = worklogMapper.toEntity(createWorklogDto);
        return worklogMapper.toResponseDto(worklogRepository.save(worklog));
    }

    public WorklogResponseDto updateWorklog(Long id, UpdateWorklogDto updateWorklogDto) {
        log.debug("Updating worklog with id: {} with data: {}", id, updateWorklogDto);
        return worklogRepository.findById(id)
                .map(existingWorklog -> {
                    Worklog updatedWorklog = worklogMapper.updateEntityFromDto(updateWorklogDto, existingWorklog);
                    return worklogMapper.toResponseDto(worklogRepository.save(updatedWorklog));
                })
                .orElseThrow(() -> new EntityNotFoundException("Worklog", id));
    }

    public void deleteWorklog(Long id) {
        log.debug("Deleting worklog by id: {}", id);
        if (worklogRepository.existsById(id)) {
            worklogRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Worklog", id);
        }
    }
}