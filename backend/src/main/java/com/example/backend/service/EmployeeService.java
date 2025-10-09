package com.example.backend.service;

import com.example.backend.dto.EmployeeDTO;

public interface EmployeeService {
    EmployeeDTO.EmployeeDto login(String email, String password);
}
