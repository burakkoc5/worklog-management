package com.kron.homework.worklog.service;

import com.kron.homework.worklog.dto.grade.CreateGradeDto;
import com.kron.homework.worklog.dto.grade.UpdateGradeDto;
import com.kron.homework.worklog.dto.grade.GradeResponseDto;
import com.kron.homework.worklog.exception.EntityNotFoundException;
import com.kron.homework.worklog.model.Grade;
import com.kron.homework.worklog.repository.GradeRepository;
import com.kron.homework.worklog.mapper.GradeMapper; // Assuming GradeMapper is in this package
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
public class GradeService extends BaseService<Grade, GradeResponseDto>  {

    private final GradeRepository gradeRepository;
    private final GradeMapper gradeMapper;

    @Autowired
    public GradeService(GradeRepository gradeRepository, GradeMapper gradeMapper) {
        this.gradeRepository = gradeRepository;
        this.gradeMapper = gradeMapper;
        log.info("GradeService initialized");
    }

    @Override
    protected JpaRepository<Grade, Long> getRepository() {
        return gradeRepository;
    }

    @Override
    protected Function<Grade, GradeResponseDto> getMapper() {
        return gradeMapper::toResponseDto;
    }

    @Override
    protected List<String> getValidSortProperties() {
        return Arrays.asList("id", "name");
    }

    public Page<GradeResponseDto> getAllGrades(Pageable pageable) {
        return getAllWithPagination(pageable);
    }

    public GradeResponseDto getGradeById(Long id) {
        log.debug("Fetching grade by id: {}", id);
        return gradeRepository.findById(id)
                .map(gradeMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Grade", id));
    }

    public GradeResponseDto saveGrade(CreateGradeDto createGradeDto) {
        log.debug("Saving grade: {}", createGradeDto);
        Grade grade = gradeMapper.toEntity(createGradeDto);
        return gradeMapper.toResponseDto(gradeRepository.save(grade));
    }

    public GradeResponseDto updateGrade(UpdateGradeDto updateGradeDto) {
        log.debug("Updating grade with id: {} with data: {}", updateGradeDto.getId(), updateGradeDto);
        return gradeRepository.findById(updateGradeDto.getId())
                .map(existingGrade -> {
                    Grade updatedGrade = gradeMapper.updateEntityFromDto(updateGradeDto, existingGrade);
                    return gradeMapper.toResponseDto(gradeRepository.save(updatedGrade));
                })
                .orElseThrow(() -> new EntityNotFoundException("Grade", updateGradeDto.getId()));
    }

    public void deleteGrade(Long id) {
        log.debug("Deleting grade with id: {}", id);
        if (gradeRepository.existsById(id)) {
            gradeRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Grade", id);
        }
    }
}