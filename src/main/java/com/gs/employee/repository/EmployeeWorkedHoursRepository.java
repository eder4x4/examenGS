package com.gs.employee.repository;

import com.gs.employee.model.entity.EmployeeWorkedHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeWorkedHoursRepository extends JpaRepository<EmployeeWorkedHours, Long> {

    Optional<EmployeeWorkedHours> findByEmployeeIdAndWorkedDate(Long employeeId, LocalDate workedDate);

    @Query("SELECT SUM(e.workedHours) FROM EmployeeWorkedHours e WHERE e.employee.id = :employeeId AND e.workedDate BETWEEN :startDate AND :endDate")
    Optional<Double> findTotalWorkedHoursByEmployeeIdAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);


}
