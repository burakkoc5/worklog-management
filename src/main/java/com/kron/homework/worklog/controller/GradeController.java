package com.kron.homework.worklog.controller;

import com.kron.homework.worklog.dto.grade.CreateGradeDto;
import com.kron.homework.worklog.dto.grade.UpdateGradeDto;
import com.kron.homework.worklog.dto.grade.GradeResponseDto;
import com.kron.homework.worklog.service.GradeService;
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
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public ResponseEntity<Page<GradeResponseDto>> getAllGrades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(gradeService.getAllWithPagination(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeResponseDto> getGradeById(@PathVariable Long id) {
        return ResponseEntity.ok(gradeService.getGradeById(id));
    }

    @PostMapping
    public ResponseEntity<GradeResponseDto> createGrade(@Valid @RequestBody CreateGradeDto createGradeDto) {
        GradeResponseDto savedGrade = gradeService.saveGrade(createGradeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeResponseDto> updateGrade(@PathVariable Long id, @Valid @RequestBody UpdateGradeDto updateGradeDto) {
        return ResponseEntity.ok(gradeService.updateGrade(updateGradeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }

}