package com.example.backend.dto;

import com.example.backend.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public EmployeeDTO.EmployeeDto toDto (Employee employee) {
        if (employee == null) return null;

        return new EmployeeDTO.EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone()
        );
    }

    public Employee toEntity(EmployeeDTO.EmployeeDto dto) {
        if (dto == null) return null;

        Employee e = new Employee();
        e.setId(dto.id());
        e.setName(dto.name());
        e.setEmail(dto.email());
        e.setPhone(dto.phone());
        return e;
    }

}
