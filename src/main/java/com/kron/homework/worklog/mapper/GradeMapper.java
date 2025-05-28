package com.kron.homework.worklog.mapper;

import com.kron.homework.worklog.dto.grade.CreateGradeDto;
import com.kron.homework.worklog.dto.grade.UpdateGradeDto;
import com.kron.homework.worklog.dto.grade.GradeResponseDto;
import com.kron.homework.worklog.model.Grade;
import org.springframework.stereotype.Component;

@Component
public class GradeMapper {

    public Grade toEntity(CreateGradeDto dto) {
        Grade grade = new Grade();
        grade.setName(dto.getName());
        return grade;
    }

    public Grade updateEntityFromDto(UpdateGradeDto dto, Grade existingGrade) {
        if (dto.getName() != null) {
            existingGrade.setName(dto.getName());
        }
        return existingGrade;
    }

    public GradeResponseDto toResponseDto(Grade grade) {
        GradeResponseDto dto = new GradeResponseDto();
        dto.setId(grade.getId());
        dto.setName(grade.getName());
        return dto;
    }
}