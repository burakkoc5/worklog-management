package com.kron.homework.worklog.dto.worklogtype;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorklogTypeDto {
    @NotNull(message = "ID is required")
    private Long id;

    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;
}