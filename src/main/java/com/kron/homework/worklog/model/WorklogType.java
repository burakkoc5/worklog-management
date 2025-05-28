package com.kron.homework.worklog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class WorklogType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Worklog type name is required")
    @Size(max = 50, message = "Worklog type name must be less than 50 characters")
    @Column(nullable = false, unique = true)
    private String name;
}