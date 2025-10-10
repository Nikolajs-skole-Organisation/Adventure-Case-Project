package com.example.backend.controller;

import com.example.backend.dto.EmployeeDTO;
import com.example.backend.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService service, EmployeeService employeeService) {
        this.service = service;
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody EmployeeDTO.LoginRequest body) {
        try {
            EmployeeDTO.EmployeeDto dto = service.login(body.email(), body.password());
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO.EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
}