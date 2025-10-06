package com.example.backend.controller;

import com.example.backend.dto.ShiftAssignmentDTO;
import com.example.backend.dto.ShiftDTO;
import com.example.backend.service.ShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public ResponseEntity<List<ShiftDTO.ShiftDto>> getAllShifts() {
        return ResponseEntity.ok(shiftService.getAllShifts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftDTO.ShiftDto> getShiftById(@PathVariable Long id) {
        return ResponseEntity.ok(shiftService.getShiftById(id));
    }

    @PostMapping("/{shiftId}/employees")
    public ResponseEntity<ShiftAssignmentDTO.ShiftAssignmentDto> assignEmployeeToShift(
            @PathVariable Long shiftId,
            @RequestBody ShiftAssignmentDTO.ShiftAssignmentDto dto) {
        ShiftAssignmentDTO.ShiftAssignmentDto created = shiftService.assignEmployeeToShift(shiftId, dto.employeeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}