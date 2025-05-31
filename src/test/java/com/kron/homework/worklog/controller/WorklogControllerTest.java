package com.kron.homework.worklog.controller;

import com.kron.homework.worklog.dto.worklog.CreateWorklogDto;
import com.kron.homework.worklog.dto.worklog.UpdateWorklogDto;
import com.kron.homework.worklog.dto.worklog.WorklogResponseDto;
import com.kron.homework.worklog.exception.EntityNotFoundException;
import com.kron.homework.worklog.service.WorklogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorklogControllerTest {

    @Mock
    private WorklogService worklogService;

    @InjectMocks
    private WorklogController worklogController;

    private LocalDateTime testCreatedAt;
    private LocalDateTime testUpdatedAt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testCreatedAt = LocalDateTime.now().minusDays(1);
        testUpdatedAt = LocalDateTime.now();
    }

    @Test
    void getAllWorklogs() {
        // Arrange
        List<WorklogResponseDto> worklogs = Arrays.asList(
            WorklogResponseDto.builder()
                .id(1L)
                .employeeId(1L)
                .employeeName("John Doe")
                .monthDate(YearMonth.of(2025,5))
                .worklogTypeId(1L)
                .worklogTypeName("Type 1")
                .effort(8)
                .createdAt(testCreatedAt)
                .updatedAt(testUpdatedAt)
                .build(),
            WorklogResponseDto.builder()
                .id(2L)
                .employeeId(2L)
                .employeeName("Jane Smith")
                .monthDate(YearMonth.of(2025,5))
                .worklogTypeId(2L)
                .worklogTypeName("Type 2")
                .effort(8)
                .createdAt(testCreatedAt)
                .updatedAt(testUpdatedAt)
                .build()
        );
        Page<WorklogResponseDto> worklogPage = new PageImpl<>(worklogs);
        when(worklogService.getAllWorklogs(any(Pageable.class))).thenReturn(worklogPage);

        // Act
        ResponseEntity<Page<WorklogResponseDto>> response = worklogController.getAllWorklogs(0, 10, "id", "asc");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(worklogPage, response.getBody());
        verify(worklogService).getAllWorklogs(any(Pageable.class));
    }

    @Test
    void getAllWorklogs_EmptyList() {
        // Arrange
        Page<WorklogResponseDto> emptyPage = new PageImpl<>(List.of());
        when(worklogService.getAllWorklogs(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        ResponseEntity<Page<WorklogResponseDto>> response = worklogController.getAllWorklogs(0, 10, "id", "asc");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getContent().isEmpty());
        verify(worklogService).getAllWorklogs(any(Pageable.class));
    }

    @Test
    void getWorklogById() {
        // Arrange
        Long worklogId = 1L;
        WorklogResponseDto worklog = WorklogResponseDto.builder()
            .id(worklogId)
            .employeeId(1L)
            .employeeName("John Doe")
            .monthDate(YearMonth.of(2025,5))
            .worklogTypeId(1L)
            .worklogTypeName("Type 1")
            .effort(8)
            .createdAt(testCreatedAt)
            .updatedAt(testUpdatedAt)
            .build();
        when(worklogService.getWorklogById(worklogId)).thenReturn(worklog);

        // Act
        ResponseEntity<WorklogResponseDto> response = worklogController.getWorklogById(worklogId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(worklog, response.getBody());
        verify(worklogService).getWorklogById(worklogId);
    }

    @Test
    void getWorklogById_NotFound() {
        // Arrange
        Long worklogId = 999L;
        when(worklogService.getWorklogById(worklogId)).thenThrow(new EntityNotFoundException("Worklog", worklogId));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            worklogController.getWorklogById(worklogId);
        });

        assertEquals("Worklog not found with id: 999", exception.getMessage());
    }

    @Test
    void createWorklog() {
        // Arrange
        CreateWorklogDto createDto = new CreateWorklogDto();
        createDto.setEmployeeId(1L);
        createDto.setWorklogTypeId(1L);
        createDto.setEffort(8);
        createDto.setMonthDate(YearMonth.of(2025, 5));

        WorklogResponseDto createdWorklog = WorklogResponseDto.builder()
            .id(1L)
            .employeeId(1L)
            .employeeName("John Doe")
            .monthDate(YearMonth.of(2025,5))
            .worklogTypeId(1L)
            .worklogTypeName("Type 1")
            .effort(8)
            .createdAt(testCreatedAt)
            .updatedAt(testUpdatedAt)
            .build();
        when(worklogService.createWorklog(any(CreateWorklogDto.class))).thenReturn(createdWorklog);

        // Act
        ResponseEntity<WorklogResponseDto> response = worklogController.createWorklog(createDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdWorklog, response.getBody());
        verify(worklogService).createWorklog(createDto);
    }

    @Test
    void createWorklog_InvalidInput() {
        // Arrange
        CreateWorklogDto createDto = new CreateWorklogDto();
        createDto.setEmployeeId(null);  // Invalid: employeeId is required
        createDto.setWorklogTypeId(1L);
        createDto.setEffort(8);
        createDto.setMonthDate(YearMonth.of(2025, 5));

        when(worklogService.createWorklog(any(CreateWorklogDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input: employeeId is required"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            worklogController.createWorklog(createDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid input: employeeId is required", exception.getReason());
        verify(worklogService).createWorklog(createDto);
    }

    @Test
    void updateWorklog() {
        // Arrange
        Long worklogId = 1L;
        UpdateWorklogDto updateDto = new UpdateWorklogDto();
        updateDto.setEmployeeId(1L);
        updateDto.setWorklogTypeId(1L);
        updateDto.setEffort(9);
        updateDto.setMonthDate(YearMonth.of(2025, 6));

        WorklogResponseDto updatedWorklog = WorklogResponseDto.builder()
            .id(worklogId)
            .employeeId(1L)
            .employeeName("John Doe")
            .monthDate(YearMonth.of(2025,6))
            .worklogTypeId(1L)
            .worklogTypeName("Type 1")
            .effort(9)
            .createdAt(testCreatedAt)
            .updatedAt(testUpdatedAt)
            .build();
        when(worklogService.updateWorklog(eq(worklogId), any(UpdateWorklogDto.class))).thenReturn(updatedWorklog);

        // Act
        ResponseEntity<WorklogResponseDto> response = worklogController.updateWorklog(worklogId, updateDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedWorklog, response.getBody());
        verify(worklogService).updateWorklog(worklogId, updateDto);
    }

    @Test
    void updateWorklog_NotFound() {
        // Arrange
        Long worklogId = 999L;
        UpdateWorklogDto updateDto = new UpdateWorklogDto();
        updateDto.setEmployeeId(1L);
        updateDto.setWorklogTypeId(1L);
        updateDto.setEffort(9);
        updateDto.setMonthDate(YearMonth.of(2025, 6));

        when(worklogService.updateWorklog(eq(worklogId), any(UpdateWorklogDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Worklog not found"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            worklogController.updateWorklog(worklogId, updateDto);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Worklog not found", exception.getReason());
        verify(worklogService).updateWorklog(worklogId, updateDto);
    }

    @Test
    void deleteWorklog() {
        // Arrange
        Long worklogId = 1L;
        doNothing().when(worklogService).deleteWorklog(worklogId);

        // Act
        ResponseEntity<Void> response = worklogController.deleteWorklog(worklogId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(worklogService).deleteWorklog(worklogId);
    }

    @Test
    void deleteWorklog_NotFound() {
        // Arrange
        Long worklogId = 999L;
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Worklog not found"))
                .when(worklogService).deleteWorklog(worklogId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            worklogController.deleteWorklog(worklogId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Worklog not found", exception.getReason());
        verify(worklogService).deleteWorklog(worklogId);
    }

    @Test
    void getWorklogsByEmployee() {
        // Arrange
        Long employeeId = 1L;
        List<WorklogResponseDto> worklogs = Arrays.asList(
            WorklogResponseDto.builder()
                .id(1L)
                .employeeId(employeeId)
                .employeeName("John Doe")
                .monthDate(YearMonth.of(2025,5))
                .worklogTypeId(1L)
                .worklogTypeName("Type 1")
                .effort(8)
                .createdAt(testCreatedAt)
                .updatedAt(testUpdatedAt)
                .build(),
            WorklogResponseDto.builder()
                .id(2L)
                .employeeId(employeeId)
                .employeeName("John Doe")
                .monthDate(YearMonth.of(2025,6))
                .worklogTypeId(2L)
                .worklogTypeName("Type 2")
                .effort(8)
                .createdAt(testCreatedAt)
                .updatedAt(testUpdatedAt)
                .build()
        );
        Page<WorklogResponseDto> worklogPage = new PageImpl<>(worklogs);
        when(worklogService.getWorklogsByEmployee(eq(employeeId), any(Pageable.class))).thenReturn(worklogPage);

        // Act
        ResponseEntity<Page<WorklogResponseDto>> response = worklogController.getWorklogsByEmployee(
            employeeId, 0, 10, "id", "asc");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(worklogPage, response.getBody());
        verify(worklogService).getWorklogsByEmployee(eq(employeeId), any(Pageable.class));
    }

    @Test
    void getWorklogsByEmployee_EmptyList() {
        // Arrange
        Long employeeId = 1L;
        Page<WorklogResponseDto> emptyPage = new PageImpl<>(List.of());
        when(worklogService.getWorklogsByEmployee(eq(employeeId), any(Pageable.class))).thenReturn(emptyPage);

        // Act
        ResponseEntity<Page<WorklogResponseDto>> response = worklogController.getWorklogsByEmployee(
            employeeId, 0, 10, "id", "asc");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getContent().isEmpty());
        verify(worklogService).getWorklogsByEmployee(eq(employeeId), any(Pageable.class));
    }
}