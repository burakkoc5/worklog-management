package com.kron.homework.worklog.dto.employee;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeDto {
    @NotNull(message = "ID is required")
    private Long id;

    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @NotNull(message = "Grade is required")
    private Long gradeId;

    @Size(max = 100, message = "Team lead name must be less than 100 characters")
    private String teamLead;

    @Size(max = 100, message = "Director name must be less than 100 characters")
    private String director;

    @Past(message = "Start date must be in the past")
    private LocalDate startDate;

    @PastOrPresent(message = "End date must be in the past or present")
    private LocalDate endDate;
}