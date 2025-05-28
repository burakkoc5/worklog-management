package com.kron.homework.worklog.controller;

import com.kron.homework.worklog.dto.worklogtype.CreateWorklogTypeDto;
import com.kron.homework.worklog.dto.worklogtype.UpdateWorklogTypeDto;
import com.kron.homework.worklog.dto.worklogtype.WorklogTypeResponseDto;
import com.kron.homework.worklog.service.WorklogTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/worklog-types")
public class WorklogTypeController {
    private final WorklogTypeService worklogTypeService;

    @Autowired
    public WorklogTypeController(WorklogTypeService worklogTypeService) {
        this.worklogTypeService = worklogTypeService;
    }

    @GetMapping
    public ResponseEntity<Page<WorklogTypeResponseDto>> getAllWorklogTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(worklogTypeService.getAllWorklogTypes(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorklogTypeResponseDto> getWorklogTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(worklogTypeService.getWorklogTypeById(id));
    }

    @PostMapping
    public ResponseEntity<WorklogTypeResponseDto> createWorklogType(@Valid @RequestBody CreateWorklogTypeDto createWorklogTypeDto) {
        WorklogTypeResponseDto createdWorklogType = worklogTypeService.createWorklogType(createWorklogTypeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorklogType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorklogTypeResponseDto> updateWorklogType(@PathVariable Long id, @Valid @RequestBody UpdateWorklogTypeDto updateWorklogTypeDto) {
        WorklogTypeResponseDto updatedWorklogType = worklogTypeService.updateWorklogType(id, updateWorklogTypeDto);
        return ResponseEntity.ok(updatedWorklogType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorklogType(@PathVariable Long id) {
        worklogTypeService.deleteWorklogType(id);
        return ResponseEntity.noContent().build();
    }
}