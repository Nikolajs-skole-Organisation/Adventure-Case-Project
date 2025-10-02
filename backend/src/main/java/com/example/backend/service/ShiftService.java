package com.example.backend.service;

import com.example.backend.dto.ShiftAssignmentDTO;

public interface ShiftService {
    ShiftAssignmentDTO.ShiftAssignmentDto assignEmployeeToShift(Long shiftId, Long employeeId);
}
