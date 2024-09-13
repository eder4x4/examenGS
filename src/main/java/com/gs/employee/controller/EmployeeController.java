package com.gs.employee.controller;

import com.gs.employee.model.dto.EmployeeDto;
import com.gs.employee.model.dto.EmployeePaymentRequestDto;
import com.gs.employee.model.dto.WorkedHoursDto;
import com.gs.employee.model.dto.WorkedHoursRequestDto;
import com.gs.employee.model.entity.Employee;
import com.gs.employee.model.entity.EmployeeWorkedHours;
import com.gs.employee.service.EmployeePaymentService;
import com.gs.employee.service.EmployeeService;
import com.gs.employee.service.EmployeeWorkedHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeWorkedHoursService workedHoursService;
    @Autowired
    private EmployeePaymentService paymentService;

    ///EJERCICIO 1
    @PostMapping("/employees")
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeDto employeeRequest) {
        try {
            Employee newEmployee = employeeService.addNewEmployee(employeeRequest);
            return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /// EJERCICIO 2
    @PostMapping("/worked-hours")
    public ResponseEntity<?> addWorkedHours(@RequestBody WorkedHoursDto request) {
        try {
            EmployeeWorkedHours workedHours = workedHoursService.addWorkedHours(request);
            return new ResponseEntity<>(workedHours, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //EJERCICIO 3
    @GetMapping("/by-job/{jobId}")
    public ResponseEntity<?> getEmployeesByJob(@PathVariable Long jobId) {
        try {
            List<Employee> employees = employeeService.getEmployeesByJob(jobId);
            if (employees.isEmpty()) {
                return new ResponseEntity<>("No hay empleados para el puesto especificado.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    ///EJERCICIO 4
    @PostMapping("/worked-hours/total")
    public ResponseEntity<?> getTotalWorkedHours(@RequestBody WorkedHoursRequestDto request) {
        try {
            Double totalWorkedHours = workedHoursService.getTotalWorkedHours(
                    request.getEmployeeId(), request.getStartDate(), request.getEndDate());
            return new ResponseEntity<>(totalWorkedHours, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    ////EJERCICIO 5
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateEmployeePayment(@RequestBody EmployeePaymentRequestDto request) {
        try {
            Double totalPayment = paymentService.calculateEmployeePayment(
                    request.getEmployeeId(), request.getStartDate(), request.getEndDate());
            return new ResponseEntity<>(totalPayment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
