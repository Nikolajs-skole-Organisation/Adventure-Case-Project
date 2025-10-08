package com.example.backend.dto;

public class EmployeeDTO {
    public record EmployeeDto(
            Long id,
            String name,
            String email,
            String phone
    ) {
    }

    public record LoginRequest(
            String email,
            String password
    ) {
    }
}
