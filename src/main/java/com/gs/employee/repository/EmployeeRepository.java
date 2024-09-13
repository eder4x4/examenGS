package com.gs.employee.repository;

import com.gs.employee.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByNameAndLastName(String name, String lastName);
    List<Employee> findByJobId(Long jobId);
    // Consulta para obtener empleados filtrados por salario y ordenados
    @Query("SELECT e FROM Employee e WHERE e.job.salary BETWEEN :minSalary AND :maxSalary")
    List<Employee> findEmployeesBySalaryRange(
            @Param("minSalary") Double minSalary,
            @Param("maxSalary") Double maxSalary,
            Pageable pageable);
}