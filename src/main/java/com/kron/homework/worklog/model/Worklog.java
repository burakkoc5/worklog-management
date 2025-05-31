package com.kron.homework.worklog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.time.LocalDateTime;

import com.kron.homework.worklog.converter.YearMonthAttributeConverter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worklog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Employee is required")
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull(message = "Month date is required")
    @Column(nullable = false)
    @Convert(converter = YearMonthAttributeConverter.class)
    private YearMonth monthDate;

    @NotNull(message = "Worklog type is required")
    @ManyToOne
    @JoinColumn(name = "worklog_type_id", nullable = false)
    private WorklogType worklogType;

    @NotNull(message = "Effort is required")
    @Min(value = 0, message = "Effort must be a positive number")
    @Column(nullable = false)
    private Integer effort;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}