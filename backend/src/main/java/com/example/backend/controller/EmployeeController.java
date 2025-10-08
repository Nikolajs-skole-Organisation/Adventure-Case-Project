package com.example.backend.controller;

import com.example.backend.dto.EmployeeDTO;
import com.example.backend.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
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
}