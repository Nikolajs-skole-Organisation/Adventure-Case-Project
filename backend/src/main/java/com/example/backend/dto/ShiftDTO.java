package com.example.backend.dto;

import java.util.List;

public class ShiftDTO {
    public record ShiftDto (Long id, String startTime, String endTime, List<EmployeeDTO.EmployeeDto> employees, boolean staffed) {}
}
