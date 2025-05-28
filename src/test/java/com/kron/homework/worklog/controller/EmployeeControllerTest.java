package com.kron.homework.worklog.controller;

import com.kron.homework.worklog.dto.employee.CreateEmployeeDto;
import com.kron.homework.worklog.dto.employee.UpdateEmployeeDto;
import com.kron.homework.worklog.dto.employee.EmployeeResponseDto;
import com.kron.homework.worklog.exception.EntityNotFoundException;
import com.kron.homework.worklog.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPagedEmployees_whenValidRequest() {
        // Arrange
        List<EmployeeResponseDto> employees = Arrays.asList(
            new EmployeeResponseDto(1L, "John", "Doe", 1L, "Grade 1", "Team Lead", "Director", LocalDate.now(), null),
            new EmployeeResponseDto(2L, "Jane", "Smith", 2L, "Grade 2", "Team Lead", "Director", LocalDate.now(), null)
        );
        Page<EmployeeResponseDto> employeePage = new PageImpl<>(employees);
        when(employeeService.getAllEmployees(any(Pageable.class))).thenReturn(employeePage);

        // Act
        ResponseEntity<Page<EmployeeResponseDto>> response = employeeController.getAllEmployees(0, 10, "id", "asc");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeePage, response.getBody());
        verify(employeeService).getAllEmployees(any(Pageable.class));
    }

    @Test
    void shouldDefaultToPage0Size10SortByIdAsc_whenParamsAreNotProvided() {
        // Act
        employeeController.getAllEmployees(0, 10, "id", "asc");

        // Assert
        verify(employeeService).getAllEmployees(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void shouldReturnEmployeesSortedDescending_whenSortDirectionIsDesc() {
        // Act
        employeeController.getAllEmployees(0, 10, "lastName", "desc");

        // Assert
        verify(employeeService).getAllEmployees(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "lastName")));
    }

    @Test
    void shouldHandleInvalidSortDirectionGracefully() {
        // Act
        employeeController.getAllEmployees(0, 10, "id", "invalid");

        // Assert
        verify(employeeService).getAllEmployees(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void shouldReturnEmployee_whenEmployeeExists() {
        // Arrange
        Long employeeId = 1L;
        EmployeeResponseDto employee = new EmployeeResponseDto(employeeId, "John", "Doe", 1L, "Grade 1", "Team Lead", "Director", LocalDate.now(), null);
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);

        // Act
        ResponseEntity<EmployeeResponseDto> response = employeeController.getEmployeeById(employeeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    void shouldReturnEntityNotFound_whenEmployeeNotFound() {
        Long employeeId = 999L;
        when(employeeService.getEmployeeById(employeeId))
                .thenThrow(new EntityNotFoundException("Employee", employeeId));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeController.getEmployeeById(employeeId);
        });
        assertEquals("Employee not found with id: 999", exception.getMessage());
    }


    @Test
    void shouldCreateEmployee_whenValidRequest() {
        // Arrange
        CreateEmployeeDto createDto = new CreateEmployeeDto();
        createDto.setFirstName("John");
        createDto.setLastName("Doe");
        EmployeeResponseDto createdEmployee = new EmployeeResponseDto(1L, "John", "Doe", 1L, "Grade 1", "Team Lead", "Director", LocalDate.now(), null);
        when(employeeService.createEmployee(any(CreateEmployeeDto.class))).thenReturn(createdEmployee);

        // Act
        ResponseEntity<EmployeeResponseDto> response = employeeController.createEmployee(createDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdEmployee, response.getBody());
    }

    @Test
    void shouldCallServiceCreateMethodOnce() {
        // Arrange
        CreateEmployeeDto createDto = new CreateEmployeeDto();
        createDto.setFirstName("John");
        createDto.setLastName("Doe");

        // Act
        employeeController.createEmployee(createDto);

        // Assert
        verify(employeeService, times(1)).createEmployee(createDto);
    }

    @Test
    void shouldUpdateEmployee_whenValidIdAndRequest() {
        // Arrange
        Long employeeId = 1L;
        UpdateEmployeeDto updateDto = new UpdateEmployeeDto();
        updateDto.setFirstName("John Updated");
        EmployeeResponseDto updatedEmployee = new EmployeeResponseDto(employeeId, "John Updated", "Doe", 1L, "Grade 1", "Team Lead", "Director", LocalDate.now(), null);
        when(employeeService.updateEmployee(eq(employeeId), any(UpdateEmployeeDto.class))).thenReturn(updatedEmployee);

        // Act
        ResponseEntity<EmployeeResponseDto> response = employeeController.updateEmployee(employeeId, updateDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedEmployee, response.getBody());
    }

    @Test
    void shouldReturnEntityNotFound_whenUpdatingNonExistentEmployee() {
        // Arrange
        Long employeeId = 999L;
        UpdateEmployeeDto updateDto = new UpdateEmployeeDto();
        when(employeeService.updateEmployee(eq(employeeId), any(UpdateEmployeeDto.class)))
                .thenThrow(new EntityNotFoundException("Employee", employeeId));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                employeeController.updateEmployee(employeeId, updateDto)
        );
        assertEquals("Employee not found with id: 999", exception.getMessage());
    }


    @Test
    void shouldDeleteEmployee_whenEmployeeExists() {
        // Arrange
        Long employeeId = 1L;
        doNothing().when(employeeService).deleteEmployee(employeeId);

        // Act
        ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }

    @Test
    void shouldReturnEntityNotFound_whenDeletingNonExistentEmployee() {
        // Arrange
        Long employeeId = 999L;
        doThrow(new EntityNotFoundException("Employee", employeeId))
                .when(employeeService).deleteEmployee(employeeId);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                employeeController.deleteEmployee(employeeId)
        );
        assertEquals("Employee not found with id: 999", exception.getMessage());
    }

}