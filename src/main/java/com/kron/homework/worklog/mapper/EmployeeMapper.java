package com.kron.homework.worklog.mapper;

import com.kron.homework.worklog.dto.employee.CreateEmployeeDto;
import com.kron.homework.worklog.dto.employee.UpdateEmployeeDto;
import com.kron.homework.worklog.dto.employee.EmployeeResponseDto;
import com.kron.homework.worklog.model.Employee;
import com.kron.homework.worklog.model.Grade;
import com.kron.homework.worklog.repository.GradeRepository;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;

@Component
public class EmployeeMapper {

    private final GradeRepository gradeRepository;

    public EmployeeMapper(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Employee toEntity(CreateEmployeeDto dto) {
        Employee employee = new Employee();
        updateEntityFromDto(employee, dto);
        return employee;
    }

    public Employee toEntity(UpdateEmployeeDto dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        updateEntityFromDto(employee, dto);
        return employee;
    }

    private void updateEntityFromDto(Employee employee, UpdateEmployeeDto dto) {
        if (dto.getFirstName() != null) employee.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) employee.setLastName(dto.getLastName());
        if (dto.getTeamLead() != null) employee.setTeamLead(dto.getTeamLead());
        if (dto.getDirector() != null) employee.setDirector(dto.getDirector());
        if (dto.getStartDate() != null) employee.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) employee.setEndDate(dto.getEndDate());

        if (dto.getGradeId() != null) {
            Grade grade = gradeRepository.findById(dto.getGradeId())
                    .orElseThrow(() -> new EntityNotFoundException("Grade not found with id: " + dto.getGradeId()));
            employee.setGrade(grade);
        }
    }
    private void updateEntityFromDto(Employee employee, CreateEmployeeDto dto) {
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setTeamLead(dto.getTeamLead());
        employee.setDirector(dto.getDirector());
        employee.setStartDate(dto.getStartDate());
        employee.setEndDate(dto.getEndDate());
        
        if (dto.getGradeId() != null) {
            Grade grade = gradeRepository.findById(dto.getGradeId())
                    .orElseThrow(() -> new EntityNotFoundException("Grade not found with id: " + dto.getGradeId()));
            employee.setGrade(grade);
        }
    }

    public EmployeeResponseDto toResponseDto(Employee employee) {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setTeamLead(employee.getTeamLead());
        dto.setDirector(employee.getDirector());
        dto.setStartDate(employee.getStartDate());
        dto.setEndDate(employee.getEndDate());
        
        if (employee.getGrade() != null) {
            dto.setGradeId(employee.getGrade().getId());
            dto.setGradeName(employee.getGrade().getName());
        }
        
        return dto;
    }

}