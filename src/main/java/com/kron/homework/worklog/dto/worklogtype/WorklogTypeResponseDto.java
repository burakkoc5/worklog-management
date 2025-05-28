package com.kron.homework.worklog.dto.worklogtype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorklogTypeResponseDto {
    private Long id;
    private String name;
}