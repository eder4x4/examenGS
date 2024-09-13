package com.gs.employee.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "EMPLOYEE_WORKED_HOURS")
public class EmployeeWorkedHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "worked_hours", nullable = false)
    private Double workedHours;

    @Column(name = "worked_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate workedDate;
}
