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
    private Long teamLeadId;
    private String teamLeadName;
    private Long directorId;
    private String directorName;
    private LocalDate startDate;
    private LocalDate endDate;
}