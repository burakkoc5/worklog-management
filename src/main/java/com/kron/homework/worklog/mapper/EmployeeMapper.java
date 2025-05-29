package com.kron.homework.worklog.mapper;

import com.kron.homework.worklog.dto.employee.CreateEmployeeDto;
import com.kron.homework.worklog.dto.employee.UpdateEmployeeDto;
import com.kron.homework.worklog.dto.employee.EmployeeResponseDto;
import com.kron.homework.worklog.model.Employee;
import com.kron.homework.worklog.model.Grade;
import com.kron.homework.worklog.repository.GradeRepository;
import com.kron.homework.worklog.repository.EmployeeRepository;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;

@Component
public class EmployeeMapper {

    private final GradeRepository gradeRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeMapper(GradeRepository gradeRepository, EmployeeRepository employeeRepository) {
        this.gradeRepository = gradeRepository;
        this.employeeRepository = employeeRepository;
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
        if (dto.getStartDate() != null) employee.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) employee.setEndDate(dto.getEndDate());

        if (dto.getGradeId() != null) {
            Grade grade = gradeRepository.findById(dto.getGradeId())
                    .orElseThrow(() -> new EntityNotFoundException("Grade not found with id: " + dto.getGradeId()));
            employee.setGrade(grade);
        }

        if (dto.getTeamLeadId() != null) {
            Employee teamLead = employeeRepository.findById(dto.getTeamLeadId())
                    .orElseThrow(() -> new EntityNotFoundException("Team Lead not found with id: " + dto.getTeamLeadId()));
            employee.setTeamLead(teamLead);
        }

        if (dto.getDirectorId() != null) {
            Employee director = employeeRepository.findById(dto.getDirectorId())
                    .orElseThrow(() -> new EntityNotFoundException("Director not found with id: " + dto.getDirectorId()));
            employee.setDirector(director);
        }
    }

    private void updateEntityFromDto(Employee employee, CreateEmployeeDto dto) {
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setStartDate(dto.getStartDate());
        employee.setEndDate(dto.getEndDate());
        
        if (dto.getGradeId() != null) {
            Grade grade = gradeRepository.findById(dto.getGradeId())
                    .orElseThrow(() -> new EntityNotFoundException("Grade not found with id: " + dto.getGradeId()));
            employee.setGrade(grade);
        }

        if (dto.getTeamLeadId() != null) {
            Employee teamLead = employeeRepository.findById(dto.getTeamLeadId())
                    .orElseThrow(() -> new EntityNotFoundException("Team Lead not found with id: " + dto.getTeamLeadId()));
            employee.setTeamLead(teamLead);
        }

        if (dto.getDirectorId() != null) {
            Employee director = employeeRepository.findById(dto.getDirectorId())
                    .orElseThrow(() -> new EntityNotFoundException("Director not found with id: " + dto.getDirectorId()));
            employee.setDirector(director);
        }
    }

    public EmployeeResponseDto toResponseDto(Employee employee) {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setStartDate(employee.getStartDate());
        dto.setEndDate(employee.getEndDate());
        
        if (employee.getGrade() != null) {
            dto.setGradeId(employee.getGrade().getId());
            dto.setGradeName(employee.getGrade().getName());
        }
        
        if (employee.getTeamLead() != null) {
            dto.setTeamLeadId(employee.getTeamLead().getId());
            dto.setTeamLeadName(employee.getTeamLead().getFirstName() + " " + employee.getTeamLead().getLastName());
        }
        
        if (employee.getDirector() != null) {
            dto.setDirectorId(employee.getDirector().getId());
            dto.setDirectorName(employee.getDirector().getFirstName() + " " + employee.getDirector().getLastName());
        }
        
        return dto;
    }

}