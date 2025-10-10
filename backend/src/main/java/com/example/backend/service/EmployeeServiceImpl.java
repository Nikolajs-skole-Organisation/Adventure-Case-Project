package com.example.backend.service;

import com.example.backend.dto.EmployeeDTO;
import com.example.backend.dto.EmployeeMapper;
import com.example.backend.model.Employee;
import com.example.backend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepo;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepo, EmployeeMapper employeeMapper) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
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

    @Override
    public List<EmployeeDTO.EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        List<EmployeeDTO.EmployeeDto> employeeDtos = new ArrayList<>();

        for (Employee e : employees) {
            EmployeeDTO.EmployeeDto dto = employeeMapper.toDto(e);

            dto = new EmployeeDTO.EmployeeDto(
                    dto.id(),
                    dto.name(),
                    dto.email(),
                    dto.phone()
            );
            employeeDtos.add(dto);
        }
        return employeeDtos;
    }
}
