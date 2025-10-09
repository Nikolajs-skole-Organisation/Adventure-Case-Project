package com.example.backend.service;

import com.example.backend.dto.EmployeeDTO;
import com.example.backend.model.Employee;
import com.example.backend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepo;

    public EmployeeServiceImpl(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public EmployeeDTO.EmployeeDto login(String email, String password) {
        Employee employee = employeeRepo.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        return new EmployeeDTO.EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone()
        );
    }
}
