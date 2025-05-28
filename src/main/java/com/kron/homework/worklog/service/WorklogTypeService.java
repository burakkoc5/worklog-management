package com.kron.homework.worklog.service;

import com.kron.homework.worklog.dto.worklogtype.CreateWorklogTypeDto;
import com.kron.homework.worklog.dto.worklogtype.UpdateWorklogTypeDto;
import com.kron.homework.worklog.dto.worklogtype.WorklogTypeResponseDto;
import com.kron.homework.worklog.exception.EntityNotFoundException;
import com.kron.homework.worklog.mapper.WorklogTypeMapper;
import com.kron.homework.worklog.model.WorklogType;
import com.kron.homework.worklog.repository.WorklogTypeRepository;
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
public class WorklogTypeService extends BaseService<WorklogType, WorklogTypeResponseDto> {

    private final WorklogTypeRepository worklogTypeRepository;
    private final WorklogTypeMapper worklogTypeMapper;

    @Autowired
    public WorklogTypeService(WorklogTypeRepository worklogTypeRepository, WorklogTypeMapper worklogTypeMapper) {
        this.worklogTypeRepository = worklogTypeRepository;
        this.worklogTypeMapper = worklogTypeMapper;
        log.info("WorklogTypeService initialized");
    }

    @Override
    protected JpaRepository<WorklogType, Long> getRepository() {
        return worklogTypeRepository;
    }

    @Override
    protected Function<WorklogType, WorklogTypeResponseDto> getMapper() {
        return worklogTypeMapper::toResponseDto;
    }

    @Override
    protected List<String> getValidSortProperties() {
        return Arrays.asList("id", "name");
    }

    public Page<WorklogTypeResponseDto> getAllWorklogTypes(Pageable pageable) {
        return getAllWithPagination(pageable);
    }

    public WorklogTypeResponseDto getWorklogTypeById(Long id) {
        log.info("Fetching worklog type with id: {}", id);
        return worklogTypeRepository.findById(id)
                .map(worklogTypeMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("WorklogType", id));
    }

    public WorklogTypeResponseDto createWorklogType(CreateWorklogTypeDto createWorklogTypeDto) {
        log.info("Creating new worklog type: {}", createWorklogTypeDto);
        WorklogType worklogType = worklogTypeMapper.toEntity(createWorklogTypeDto);
        WorklogTypeResponseDto savedDto = worklogTypeMapper.toResponseDto(worklogTypeRepository.save(worklogType));
        log.debug("Created worklog type with id: {}", savedDto.getId());
        return savedDto;
    }

    public WorklogTypeResponseDto updateWorklogType(Long id, UpdateWorklogTypeDto updateWorklogTypeDto) {
        log.info("Updating worklog type with id: {}", id);
        return worklogTypeRepository.findById(id)
                .map(existingWorklogType -> {
                    WorklogType updatedWorklogType = worklogTypeMapper.updateEntityFromDto(updateWorklogTypeDto, existingWorklogType);
                    WorklogTypeResponseDto updatedDto = worklogTypeMapper.toResponseDto(worklogTypeRepository.save(updatedWorklogType));
                    log.debug("Updated worklog type: {}", updatedDto);
                    return updatedDto;
                })
                .orElseThrow(() -> new EntityNotFoundException("WorklogType", id));
    }

    public void deleteWorklogType(Long id) {
        log.info("Deleting worklog type with id: {}", id);
        if (worklogTypeRepository.existsById(id)) {
            worklogTypeRepository.deleteById(id);
            log.debug("Deleted worklog type with id: {}", id);
        } else {
            throw new EntityNotFoundException("WorklogType", id);
        }
    }
}