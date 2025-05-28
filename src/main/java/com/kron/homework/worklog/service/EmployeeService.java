package com.kron.homework.worklog.service;

import com.kron.homework.worklog.dto.employee.CreateEmployeeDto;
import com.kron.homework.worklog.dto.employee.UpdateEmployeeDto;
import com.kron.homework.worklog.dto.employee.EmployeeResponseDto;
import com.kron.homework.worklog.exception.EntityNotFoundException;
import com.kron.homework.worklog.mapper.EmployeeMapper;
import com.kron.homework.worklog.model.Employee;
import com.kron.homework.worklog.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        log.info("EmployeeService initialized");
    }

    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(employeeMapper::toResponseDto);
    }

    public EmployeeResponseDto getEmployeeById(Long id) {
        log.debug("Fetching employee by id: {}", id);
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Employee", id));
    }

    public EmployeeResponseDto createEmployee(CreateEmployeeDto createEmployeeDto) {
        log.debug("Creating employee: {}", createEmployeeDto);
        Employee employee = employeeMapper.toEntity(createEmployeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDto(savedEmployee);
    }

    public EmployeeResponseDto updateEmployee(Long id, UpdateEmployeeDto updateEmployeeDto) {
        log.debug("Updating employee with id: {} with data: {}", id, updateEmployeeDto);
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee", id);
        }
        updateEmployeeDto.setId(id);
        Employee employee = employeeMapper.toEntity(updateEmployeeDto);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDto(updatedEmployee);
    }

    public void deleteEmployee(Long id) {
        log.debug("Deleting employee by id: {}", id);
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee", id);
        }
        employeeRepository.deleteById(id);
    }
}