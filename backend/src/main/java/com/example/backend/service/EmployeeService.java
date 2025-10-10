package com.example.backend.service;

import com.example.backend.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO.EmployeeDto login(String email, String password);
    List<EmployeeDTO.EmployeeDto> getAllEmployees();
}
