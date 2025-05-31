package com.kron.homework.worklog.mapper;

import com.kron.homework.worklog.dto.worklog.CreateWorklogDto;
import com.kron.homework.worklog.dto.worklog.UpdateWorklogDto;
import com.kron.homework.worklog.dto.worklog.WorklogResponseDto;
import com.kron.homework.worklog.model.Worklog;
import com.kron.homework.worklog.model.Employee;
import com.kron.homework.worklog.model.WorklogType;
import com.kron.homework.worklog.repository.EmployeeRepository;
import com.kron.homework.worklog.repository.WorklogTypeRepository;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;

@Component
public class WorklogMapper {

    private final EmployeeRepository employeeRepository;
    private final WorklogTypeRepository worklogTypeRepository;

    public WorklogMapper(EmployeeRepository employeeRepository, WorklogTypeRepository worklogTypeRepository) {
        this.employeeRepository = employeeRepository;
        this.worklogTypeRepository = worklogTypeRepository;
    }

    public Worklog toEntity(CreateWorklogDto dto) {
        Worklog worklog = new Worklog();
        updateEntityFromDto(worklog, dto);
        return worklog;
    }

    public Worklog updateEntityFromDto(UpdateWorklogDto dto, Worklog existingWorklog) {
        if (dto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
            existingWorklog.setEmployee(employee);
        }
        if (dto.getMonthDate() != null) {
            existingWorklog.setMonthDate(dto.getMonthDate());
        }
        if (dto.getWorklogTypeId() != null) {
            WorklogType worklogType = worklogTypeRepository.findById(dto.getWorklogTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("WorklogType not found with id: " + dto.getWorklogTypeId()));
            existingWorklog.setWorklogType(worklogType);
        }
        if (dto.getEffort() != null) {
            existingWorklog.setEffort(dto.getEffort());
        }

        return existingWorklog;
    }

    private void updateEntityFromDto(Worklog worklog, CreateWorklogDto dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
        worklog.setEmployee(employee);

        worklog.setMonthDate(dto.getMonthDate());

        WorklogType worklogType = worklogTypeRepository.findById(dto.getWorklogTypeId())
                .orElseThrow(() -> new EntityNotFoundException("WorklogType not found with id: " + dto.getWorklogTypeId()));
        worklog.setWorklogType(worklogType);

        worklog.setEffort(dto.getEffort());
    }

    public WorklogResponseDto toResponseDto(Worklog worklog) {
        WorklogResponseDto dto = new WorklogResponseDto();
        dto.setId(worklog.getId());
        dto.setEmployeeId(worklog.getEmployee().getId());
        dto.setEmployeeName(worklog.getEmployee().getFirstName() + " " + worklog.getEmployee().getLastName());
        dto.setMonthDate(worklog.getMonthDate());
        dto.setWorklogTypeId(worklog.getWorklogType().getId());
        dto.setWorklogTypeName(worklog.getWorklogType().getName());
        dto.setEffort(worklog.getEffort());
        dto.setCreatedAt(worklog.getCreatedAt());
        dto.setUpdatedAt(worklog.getUpdatedAt());
        return dto;
    }
}