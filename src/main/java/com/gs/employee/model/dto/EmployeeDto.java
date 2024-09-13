package com.gs.employee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDto {

    private String name;
    private String lastName;
    private String birthdate; // Formato YYYY-MM-DD
    private Long genderId;
    private Long jobId;
}
