package com.kron.homework.worklog.controller;

import com.kron.homework.worklog.dto.worklog.CreateWorklogDto;
import com.kron.homework.worklog.dto.worklog.UpdateWorklogDto;
import com.kron.homework.worklog.dto.worklog.WorklogResponseDto;
import com.kron.homework.worklog.service.WorklogService;
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
@RequestMapping("/api/worklogs")
public class WorklogController {

    private final WorklogService worklogService;

    @Autowired
    public WorklogController(WorklogService worklogService) {
        this.worklogService = worklogService;
    }

    @GetMapping
    public ResponseEntity<Page<WorklogResponseDto>> getAllWorklogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        return ResponseEntity.ok(worklogService.getAllWorklogs(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorklogResponseDto> getWorklogById(@PathVariable Long id) {
        return ResponseEntity.ok(worklogService.getWorklogById(id));
    }

    @PostMapping
    public ResponseEntity<WorklogResponseDto> createWorklog(@Valid @RequestBody CreateWorklogDto createWorklogDto) {
        WorklogResponseDto savedWorklog = worklogService.createWorklog(createWorklogDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWorklog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorklogResponseDto> updateWorklog(@PathVariable Long id, @Valid @RequestBody UpdateWorklogDto updateWorklogDto) {
        return ResponseEntity.ok(worklogService.updateWorklog(id, updateWorklogDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorklog(@PathVariable Long id) {
        worklogService.deleteWorklog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<WorklogResponseDto>> getWorklogsByEmployee(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        return ResponseEntity.ok(worklogService.getWorklogsByEmployee(employeeId, pageable));
    }
}