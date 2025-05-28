package com.kron.homework.worklog.controller;

import com.kron.homework.worklog.dto.grade.CreateGradeDto;
import com.kron.homework.worklog.dto.grade.UpdateGradeDto;
import com.kron.homework.worklog.dto.grade.GradeResponseDto;
import com.kron.homework.worklog.exception.EntityNotFoundException;
import com.kron.homework.worklog.service.GradeService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// TODO: Use @WebMvcTest with MockMvc to test validation properly,
class GradeControllerTest {

    @Mock
    private GradeService gradeService;

    @InjectMocks
    private GradeController gradeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPagedGrades_whenValidRequest() {
        // Arrange
        List<GradeResponseDto> grades = Arrays.asList(
            new GradeResponseDto(1L, "Grade 1"),
            new GradeResponseDto(2L, "Grade 2")
        );
        Page<GradeResponseDto> gradePage = new PageImpl<>(grades);
        when(gradeService.getAllWithPagination(any(Pageable.class))).thenReturn(gradePage);

        // Act
        ResponseEntity<Page<GradeResponseDto>> response = gradeController.getAllGrades(0, 10, "id", "asc");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gradePage, response.getBody());
        verify(gradeService).getAllWithPagination(any(Pageable.class));
    }

    @Test
    void shouldDefaultToPage0Size10SortByIdAsc_whenParamsAreNotProvided() {
        // Act
        gradeController.getAllGrades(0, 10, "id", "asc");

        // Assert
        verify(gradeService).getAllWithPagination(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void shouldReturnGradesSortedDescending_whenSortDirectionIsDesc() {
        // Act
        gradeController.getAllGrades(0, 10, "name", "desc");

        // Assert
        verify(gradeService).getAllWithPagination(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name")));
    }

    @Test
    void shouldReturnGrade_whenGradeExists() {
        // Arrange
        Long gradeId = 1L;
        GradeResponseDto grade = new GradeResponseDto(gradeId, "Grade 1");
        when(gradeService.getGradeById(gradeId)).thenReturn(grade);

        // Act
        ResponseEntity<GradeResponseDto> response = gradeController.getGradeById(gradeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grade, response.getBody());
    }

    @Test
    void shouldReturn404_whenGradeNotFound() {
        // Arrange
        Long gradeId = 999L;
        when(gradeService.getGradeById(gradeId)).thenThrow(new EntityNotFoundException("Grade", gradeId));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            gradeController.getGradeById(gradeId);
        });
        assertEquals("Grade not found with id: 999", exception.getMessage());
    }

    @Test
    void shouldCreateGrade_whenValidRequest() {
        // Arrange
        CreateGradeDto createDto = new CreateGradeDto();
        createDto.setName("New Grade");
        GradeResponseDto createdGrade = new GradeResponseDto(1L, "New Grade");
        when(gradeService.saveGrade(any(CreateGradeDto.class))).thenReturn(createdGrade);

        // Act
        ResponseEntity<GradeResponseDto> response = gradeController.createGrade(createDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdGrade, response.getBody());
    }

    @Test
    void shouldReturn400_whenValidationFailsOnCreate() {
        // Arrange
        CreateGradeDto createDto = new CreateGradeDto();
        // Assuming name is required
        createDto.setName("");

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> gradeController.createGrade(createDto));
    }

    @Test
    void shouldUpdateGrade_whenValidRequest() {
        // Arrange
        Long gradeId = 1L;
        UpdateGradeDto updateDto = new UpdateGradeDto();
        updateDto.setId(gradeId);
        updateDto.setName("Updated Grade");
        GradeResponseDto updatedGrade = new GradeResponseDto(gradeId, "Updated Grade");
        when(gradeService.updateGrade(any(UpdateGradeDto.class))).thenReturn(updatedGrade);

        // Act
        ResponseEntity<GradeResponseDto> response = gradeController.updateGrade(gradeId, updateDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedGrade, response.getBody());
    }

    @Test
    void shouldReturn404_whenUpdatingNonExistentGrade() {
        // Arrange
        Long gradeId = 999L;
        UpdateGradeDto updateDto = new UpdateGradeDto();
        updateDto.setId(gradeId);
        when(gradeService.updateGrade(any(UpdateGradeDto.class)))
            .thenThrow(new EntityNotFoundException("Grade", gradeId));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> gradeController.updateGrade(gradeId, updateDto));

        assertEquals( "Grade not found with id: 999", exception.getMessage());
    }

    @Test
    void shouldReturn400_whenValidationFailsOnUpdate() {
        // Arrange
        Long gradeId = 1L;
        UpdateGradeDto updateDto = new UpdateGradeDto();
        updateDto.setId(gradeId);
        // Assuming name is required
        updateDto.setName("");

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> gradeController.updateGrade(gradeId, updateDto));
    }

    @Test
    void shouldDeleteGrade_whenGradeExists() {
        // Arrange
        Long gradeId = 1L;
        doNothing().when(gradeService).deleteGrade(gradeId);

        // Act
        ResponseEntity<Void> response = gradeController.deleteGrade(gradeId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(gradeService).deleteGrade(gradeId);
    }

    @Test
    void shouldReturnEntityNotFound_whenDeletingNonExistentGrade() {
        // Arrange
        Long gradeId = 999L;
        doThrow(new EntityNotFoundException("Grade", gradeId)).when(gradeService).deleteGrade(gradeId);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            gradeController.deleteGrade(gradeId);
        });

        assertEquals("Grade not found with id: 999", exception.getMessage());
    }
}