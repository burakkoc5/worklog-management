package com.kron.homework.worklog.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long gradeId;
    private String gradeName;
    private String teamLead;
    private String director;
    private LocalDate startDate;
    private LocalDate endDate;
}