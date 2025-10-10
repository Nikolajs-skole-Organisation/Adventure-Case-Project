package com.example.backend.service;

import com.example.backend.dto.ShiftAssignmentDTO;
import com.example.backend.dto.ShiftDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftService {
    List<ShiftDTO.ShiftDto> getAllShifts();
    ShiftDTO.ShiftDto getShiftById(Long shiftId);
    ShiftAssignmentDTO.ShiftAssignmentDto assignEmployeeToShift(Long shiftId, Long employeeId);
    void unassignEmployeeFromShift (Long shiftId, Long employeeId);
    List<ShiftDTO.ShiftDto> getShiftsBetween(LocalDateTime start, LocalDateTime end);
}
