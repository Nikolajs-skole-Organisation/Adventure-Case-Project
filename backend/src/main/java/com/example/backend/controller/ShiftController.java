package com.example.backend.controller;

import com.example.backend.dto.ShiftAssignmentDTO;
import com.example.backend.dto.ShiftDTO;
import com.example.backend.service.ShiftService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GetMapping("/overview")
    public ResponseEntity<List<ShiftDTO.ShiftDto>> getWeeklyOverview(
            @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate weekStart) {
        LocalDateTime start = weekStart.atStartOfDay();
        LocalDateTime end = weekStart.plusDays(7).atStartOfDay();
        List<ShiftDTO.ShiftDto> result = shiftService.getShiftsBetween(start, end);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{shiftId}")
    public ResponseEntity<ShiftDTO.ShiftDto> getShiftById(@PathVariable Long shiftId) {
        return ResponseEntity.ok(shiftService.getShiftById(shiftId));
    }

    @PostMapping("/{shiftId}/employees")
    public ResponseEntity<ShiftAssignmentDTO.ShiftAssignmentDto> assignEmployeeToShift(
            @PathVariable Long shiftId,
            @RequestBody ShiftAssignmentDTO.ShiftAssignmentDto dto) {
        ShiftAssignmentDTO.ShiftAssignmentDto created = shiftService.assignEmployeeToShift(shiftId, dto.employeeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping ("/{shiftId}/employees/{employeeId}")
    public ResponseEntity<Void> unassignEmployeeFromShift(
            @PathVariable Long shiftId,
            @PathVariable Long employeeId) {
        shiftService.unassignEmployeeFromShift(shiftId, employeeId);
        return ResponseEntity.noContent().build();
    }
}