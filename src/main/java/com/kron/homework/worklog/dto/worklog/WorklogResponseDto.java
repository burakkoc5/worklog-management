package com.kron.homework.worklog.dto.worklog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorklogResponseDto {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private YearMonth monthDate;
    private Long worklogTypeId;
    private String worklogTypeName;
    private Integer effort;
}