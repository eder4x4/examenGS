package com.gs.employee.service;

import com.gs.employee.model.dto.EmployeeDto;
import com.gs.employee.model.dto.EmployeeResponseDto;
import com.gs.employee.model.entity.Employee;
import com.gs.employee.model.entity.Gender;
import com.gs.employee.model.entity.Job;
import com.gs.employee.repository.EmployeeRepository;
import com.gs.employee.repository.GenderRepository;
import com.gs.employee.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private JobRepository jobRepository;

    public Employee addNewEmployee(EmployeeDto employeeRequest) throws Exception {
        // Verificar si ya existe un empleado con el mismo nombre y apellido
        Optional<Employee> existingEmployee = employeeRepository.findByNameAndLastName(
                employeeRequest.getName(), employeeRequest.getLastName());
        if (existingEmployee.isPresent()) {
            throw new Exception("El empleado con el mismo nombre y apellido ya existe.");
        }

        // Validar que el empleado sea mayor de edad
        LocalDate birthdate = LocalDate.parse(employeeRequest.getBirthdate());
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < 18) {
            throw new Exception("El empleado debe ser mayor de edad.");
        }

        // Validar que el género exista
        boolean genderExists = genderRepository.existsById(employeeRequest.getGenderId());
        if (!genderExists) {
            throw new Exception("El género no existe.");
        }

        // Validar que el puesto exista
        boolean jobExists = jobRepository.existsById(employeeRequest.getJobId());
        if (!jobExists) {
            throw new Exception("El puesto no existe.");
        }

        // Si todas las validaciones pasan, creamos el nuevo empleado
        Employee newEmployee = new Employee();
        newEmployee.setName(employeeRequest.getName());
        newEmployee.setLastName(employeeRequest.getLastName());
        newEmployee.setBirthdate(birthdate);
        newEmployee.setGender(genderRepository.findById(employeeRequest.getGenderId()).get());
        newEmployee.setJob(jobRepository.findById(employeeRequest.getJobId()).get());

        return employeeRepository.save(newEmployee);
    }

    public List<Employee> getEmployeesByJob(Long jobId) throws Exception {
        // Validar que el puesto exista
        boolean jobExists = jobRepository.existsById(jobId);
        if (!jobExists) {
            throw new Exception("El puesto no existe.");
        }

        // Devolver los empleados por puesto
        return employeeRepository.findByJobId(jobId);
    }

    public List<EmployeeResponseDto> getEmployeesBySalaryRange(Double minSalary, Double maxSalary, String order, Integer size) throws Exception {
        // Validar parámetro de orden
        if (!"asc".equalsIgnoreCase(order) && !"desc".equalsIgnoreCase(order)) {
            throw new Exception("Parámetro 'order' debe ser 'asc' o 'desc'.");
        }

        // Validar tamaño
        if (size <= 0) {
            throw new Exception("El tamaño debe ser un número positivo.");
        }

        // Configurar orden y tamaño
        Sort sort = Sort.by(Sort.Direction.fromString(order), "job.salary");
        Pageable pageable = PageRequest.of(0, size, sort);

        // Obtener empleados por rango de salario
        List<Employee> employees = employeeRepository.findEmployeesBySalaryRange(minSalary, maxSalary, pageable);

        // Convertir empleados a DTOs
        return employees.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private EmployeeResponseDto convertToDto(Employee employee) {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setLastName(employee.getLastName());

        // Mapeo del Job
        Job jobDto = new Job();
        jobDto.setId(employee.getJob().getId());
        jobDto.setName(employee.getJob().getName());
        jobDto.setSalary(employee.getJob().getSalary());
        dto.setJob(jobDto);

        // Mapeo del Gender
        Gender genderDto = new Gender();
        genderDto.setId(employee.getGender().getId());
        genderDto.setName(employee.getGender().getName());
        dto.setGender(genderDto);

        return dto;
    }
}
