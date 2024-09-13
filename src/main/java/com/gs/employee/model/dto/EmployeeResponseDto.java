package com.gs.employee.model.dto;

import com.gs.employee.model.entity.Gender;
import com.gs.employee.model.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private Job job;
    private Gender gender;
}
