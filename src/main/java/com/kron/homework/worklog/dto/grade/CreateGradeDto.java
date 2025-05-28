package com.kron.homework.worklog.dto.grade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGradeDto {
    @NotBlank(message = "Grade name is required")
    @Size(max = 50, message = "Grade name must be less than 50 characters")
    private String name;
}