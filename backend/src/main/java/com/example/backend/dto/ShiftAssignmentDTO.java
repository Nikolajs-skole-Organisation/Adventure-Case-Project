package com.example.backend.dto;

public class ShiftAssignmentDTO {
    public record ShiftAssignmentDto(Long shiftId, Long employeeId, String employeeName, String email, String phone) {}
}
