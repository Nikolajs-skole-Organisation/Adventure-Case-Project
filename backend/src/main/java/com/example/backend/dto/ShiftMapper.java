package com.example.backend.dto;

import com.example.backend.model.Employee;
import com.example.backend.model.Shift;
import org.springframework.stereotype.Component;

@Component
public class ShiftMapper {
    public ShiftAssignmentDTO.ShiftAssignmentDto toDto (Shift shift, Employee employee ) {
        if (shift == null || employee == null) return null;
        return  new ShiftAssignmentDTO.ShiftAssignmentDto(
                shift.getId(),
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone()
        );
    }
}
