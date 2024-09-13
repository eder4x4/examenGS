package com.gs.employee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkedHoursDto {

    private Long employeeId;
    private Double workedHours;
    private String workedDate;
}
