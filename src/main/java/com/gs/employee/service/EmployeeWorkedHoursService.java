package com.gs.employee.service;

import com.gs.employee.model.dto.WorkedHoursDto;
import com.gs.employee.model.entity.Employee;
import com.gs.employee.model.entity.EmployeeWorkedHours;
import com.gs.employee.repository.EmployeeRepository;
import com.gs.employee.repository.EmployeeWorkedHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmployeeWorkedHoursService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeWorkedHoursRepository employeeWorkedHoursRepository;

    public EmployeeWorkedHours addWorkedHours(WorkedHoursDto request) throws Exception {
        // Validar que el empleado exista
        Optional<Employee> employee = employeeRepository.findById(request.getEmployeeId());
        if (!employee.isPresent()) {
            throw new Exception("El empleado no existe.");
        }

        // Validar que las horas trabajadas no sean mayores a 20
        if (request.getWorkedHours() > 20) {
            throw new Exception("El total de horas trabajadas no puede ser mayor a 20.");
        }

        // Validar que la fecha de trabajo no sea mayor a la fecha actual
        LocalDate workedDate = LocalDate.parse(request.getWorkedDate());
        if (workedDate.isAfter(LocalDate.now())) {
            throw new Exception("La fecha de trabajo no puede ser mayor a la fecha actual.");
        }

        // Validar que no exista un registro de horas trabajadas en la misma fecha para el mismo empleado
        Optional<EmployeeWorkedHours> existingRecord = employeeWorkedHoursRepository
                .findByEmployeeIdAndWorkedDate(request.getEmployeeId(), workedDate);
        if (existingRecord.isPresent()) {
            throw new Exception("Ya existe un registro de horas trabajadas para este empleado en la fecha indicada.");
        }

        // Si todas las validaciones pasan, creamos el nuevo registro de horas trabajadas
        EmployeeWorkedHours workedHours = new EmployeeWorkedHours();
        workedHours.setEmployee(employee.get());
        workedHours.setWorkedHours(request.getWorkedHours());
        workedHours.setWorkedDate(workedDate);

        return employeeWorkedHoursRepository.save(workedHours);
    }

    public Double getTotalWorkedHours(Long employeeId, String startDateStr, String endDateStr) throws Exception {
        // Validar que el empleado exista
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (!employee.isPresent()) {
            throw new Exception("El empleado no existe.");
        }

        // Convertir las fechas a LocalDate
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Validar que la fecha de inicio sea menor o igual a la fecha de fin
        if (startDate.isAfter(endDate)) {
            throw new Exception("La fecha de inicio no puede ser mayor a la fecha de fin.");
        }

        // Obtener el total de horas trabajadas en el rango de fechas
        Optional<Double> totalWorkedHours = employeeWorkedHoursRepository.findTotalWorkedHoursByEmployeeIdAndDateRange(employeeId, startDate, endDate);

        // Si no se encontraron horas trabajadas, devolver 0
        return totalWorkedHours.orElse(0.0);
    }
}
