package com.gs.employee.service;

import com.gs.employee.model.entity.Employee;
import com.gs.employee.repository.EmployeeRepository;
import com.gs.employee.repository.EmployeeWorkedHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmployeePaymentService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeWorkedHoursRepository employeeWorkedHoursRepository;

    public Double calculateEmployeePayment(Long employeeId, String startDateStr, String endDateStr) throws Exception {
        // Validar que el empleado exista
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (!employeeOpt.isPresent()) {
            throw new Exception("El empleado no existe.");
        }

        Employee employee = employeeOpt.get();
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Validar que la fecha de inicio no sea mayor a la fecha de fin
        if (startDate.isAfter(endDate)) {
            throw new Exception("La fecha de inicio no puede ser mayor a la fecha de fin.");
        }

        // Obtener el total de horas trabajadas en el rango de fechas
        Optional<Double> totalWorkedHoursOpt = employeeWorkedHoursRepository.findTotalWorkedHoursByEmployeeIdAndDateRange(employeeId, startDate, endDate);
        Double totalWorkedHours = totalWorkedHoursOpt.orElse(0.0);

        // Calcular el pago total (horas trabajadas * salario por hora)
        Double salaryPerHour = employee.getJob().getSalary();
        return totalWorkedHours * salaryPerHour;
    }
}
