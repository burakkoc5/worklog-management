package com.kron.homework.worklog.dto.worklog;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorklogDto {
    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Month date is required")
    private YearMonth monthDate;

    @NotNull(message = "Worklog type ID is required")
    private Long worklogTypeId;

    @NotNull(message = "Effort is required")
    @Min(value = 0, message = "Effort must be a positive number")
    private Integer effort;
}