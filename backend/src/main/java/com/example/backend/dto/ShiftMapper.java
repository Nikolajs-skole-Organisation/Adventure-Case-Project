package com.example.backend.dto;

import com.example.backend.model.Employee;
import com.example.backend.model.Shift;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShiftMapper {

    public ShiftDTO.ShiftDto toDto (Shift shift) {
        if (shift == null) return null;

        List<EmployeeDTO.EmployeeDto> employees = new ArrayList<>();
        for (Employee e : shift.getEmployees()) {
            employees.add(new EmployeeDTO.EmployeeDto(e.getId(), e.getName(), e.getEmail(),e.getPhone()));
        }

        return new ShiftDTO.ShiftDto(
                shift.getId(),
                shift.getStartTime().toString(),
                shift.getEndTime().toString(),
                employees
        );
    }





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
