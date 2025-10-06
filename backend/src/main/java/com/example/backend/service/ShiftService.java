package com.example.backend.service;

import com.example.backend.dto.ShiftAssignmentDTO;
import com.example.backend.dto.ShiftDTO;

import java.util.List;

public interface ShiftService {
    List<ShiftDTO.ShiftDto> getAllShifts();
    ShiftDTO.ShiftDto getShiftById(Long shiftId);
    ShiftAssignmentDTO.ShiftAssignmentDto assignEmployeeToShift(Long shiftId, Long employeeId);
}
