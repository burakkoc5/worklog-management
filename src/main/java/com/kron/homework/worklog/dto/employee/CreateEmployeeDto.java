package com.kron.homework.worklog.dto.employee;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDto {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @NotNull(message = "Grade ID is required")
    private Long gradeId;

    @NotBlank(message = "Team lead is required")
    @Size(max = 100, message = "Team lead name must be less than 100 characters")
    private String teamLead;

    @NotBlank(message = "Director is required")
    @Size(max = 100, message = "Director name must be less than 100 characters")
    private String director;

    @NotNull(message = "Start date is required")
    @Past(message = "Start date must be in the past")
    private LocalDate startDate;

    private LocalDate endDate;
}