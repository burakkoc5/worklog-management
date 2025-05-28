package com.kron.homework.worklog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Grade name is required")
    @Size(max = 50, message = "Grade name must be less than 50 characters")
    @Column(nullable = false, unique = true)
    private String name;
}