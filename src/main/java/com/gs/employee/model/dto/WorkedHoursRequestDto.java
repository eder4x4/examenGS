package com.gs.employee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkedHoursRequestDto {
    private Long employeeId;
    private String startDate; // Formato YYYY-MM-DD
    private String endDate;
}
