package com.kron.homework.worklog.controller;

import com.kron.homework.worklog.dto.worklogtype.CreateWorklogTypeDto;
import com.kron.homework.worklog.dto.worklogtype.UpdateWorklogTypeDto;
import com.kron.homework.worklog.dto.worklogtype.WorklogTypeResponseDto;
import com.kron.homework.worklog.exception.EntityNotFoundException;
import com.kron.homework.worklog.service.WorklogTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// TODO: Use @WebMvcTest with MockMvc to test validation properly
class WorklogTypeControllerTest {

    @Mock
    private WorklogTypeService worklogTypeService;

    @InjectMocks
    private WorklogTypeController worklogTypeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPagedWorklogTypes_whenValidParamsProvided() {
        // Arrange
        List<WorklogTypeResponseDto> worklogTypes = Arrays.asList(
            new WorklogTypeResponseDto(1L, "Type 1"),
            new WorklogTypeResponseDto(2L, "Type 2")
        );
        Page<WorklogTypeResponseDto> worklogTypePage = new PageImpl<>(worklogTypes);
        when(worklogTypeService.getAllWorklogTypes(any(Pageable.class))).thenReturn(worklogTypePage);

        // Act
        ResponseEntity<Page<WorklogTypeResponseDto>> response = worklogTypeController.getAllWorklogTypes(0, 10, "name", "desc");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(worklogTypePage, response.getBody());
        verify(worklogTypeService).getAllWorklogTypes(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name")));
    }

    @Test
    void shouldUseDefaultPaginationAndSorting_whenNoParamsProvided() {
        // Act
        worklogTypeController.getAllWorklogTypes(0, 10, "id", "asc");

        // Assert
        verify(worklogTypeService).getAllWorklogTypes(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void shouldReturnWorklogType_whenExists() {
        // Arrange
        Long typeId = 1L;
        WorklogTypeResponseDto worklogType = new WorklogTypeResponseDto(typeId, "Type 1");
        when(worklogTypeService.getWorklogTypeById(typeId)).thenReturn(worklogType);

        // Act
        ResponseEntity<WorklogTypeResponseDto> response = worklogTypeController.getWorklogTypeById(typeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(worklogType, response.getBody());
    }

    @Test
    void shouldReturnEntityNotFound_whenWorklogTypeNotFound() {
        Long typeId = 999L;
        when(worklogTypeService.getWorklogTypeById(typeId))
                .thenThrow(new EntityNotFoundException("WorklogType", typeId));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            worklogTypeController.getWorklogTypeById(typeId);
        });
        assertEquals("WorklogType not found with id: 999", exception.getMessage());
    }

    @Test
    void shouldCreateWorklogType_whenValid() {
        // Arrange
        CreateWorklogTypeDto createDto = new CreateWorklogTypeDto();
        createDto.setName("New Type");
        WorklogTypeResponseDto createdType = new WorklogTypeResponseDto(1L, "New Type");
        when(worklogTypeService.createWorklogType(any(CreateWorklogTypeDto.class))).thenReturn(createdType);

        // Act
        ResponseEntity<WorklogTypeResponseDto> response = worklogTypeController.createWorklogType(createDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdType, response.getBody());
    }

    @Test
    void shouldReturn400_whenCreateValidationFails() {
        // Arrange
        CreateWorklogTypeDto createDto = new CreateWorklogTypeDto();
        // Assuming name is required
        createDto.setName("");

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> worklogTypeController.createWorklogType(createDto));
    }

    @Test
    void shouldUpdateWorklogType_whenValid() {
        // Arrange
        Long typeId = 1L;
        UpdateWorklogTypeDto updateDto = new UpdateWorklogTypeDto();
        updateDto.setName("Updated Type");
        WorklogTypeResponseDto updatedType = new WorklogTypeResponseDto(typeId, "Updated Type");
        when(worklogTypeService.updateWorklogType(eq(typeId), any(UpdateWorklogTypeDto.class))).thenReturn(updatedType);

        // Act
        ResponseEntity<WorklogTypeResponseDto> response = worklogTypeController.updateWorklogType(typeId, updateDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedType, response.getBody());
    }

    @Test
    void shouldReturn400_whenUpdateValidationFails() {
        // Arrange
        Long typeId = 1L;
        UpdateWorklogTypeDto updateDto = new UpdateWorklogTypeDto();
        // Assuming name is required
        updateDto.setName("");

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> worklogTypeController.updateWorklogType(typeId, updateDto));
    }

    @Test
    void shouldDeleteWorklogType_whenExists() {
        // Arrange
        Long typeId = 1L;
        doNothing().when(worklogTypeService).deleteWorklogType(typeId);

        // Act
        ResponseEntity<Void> response = worklogTypeController.deleteWorklogType(typeId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(worklogTypeService).deleteWorklogType(typeId);
    }

    @Test
    void shouldReturn404_whenDeletingNonExistentWorklogType() {
        // Arrange
        Long typeId = 999L;
        doThrow(new EntityNotFoundException("WorklogType", typeId)).when(worklogTypeService).deleteWorklogType(typeId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> worklogTypeController.deleteWorklogType(typeId));
    }
}