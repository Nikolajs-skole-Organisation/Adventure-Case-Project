package com.example.backend.service;

import com.example.backend.dto.ShiftAssignmentDTO;
import com.example.backend.dto.ShiftDTO;
import com.example.backend.dto.ShiftMapper;
import com.example.backend.exception.EmployeeAlreadyAssignedException;
import com.example.backend.exception.EmployeeNotAssignedException;
import com.example.backend.exception.EmployeeNotFoundException;
import com.example.backend.exception.OverlappingShiftException;
import com.example.backend.exception.ShiftNotFoundException;
import com.example.backend.model.Employee;
import com.example.backend.model.Shift;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.ShiftRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftServiceImpl implements ShiftService {
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftMapper shiftMapper;

    public ShiftServiceImpl(ShiftRepository shiftRepository, EmployeeRepository employeeRepository, ShiftMapper shiftMapper) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.shiftMapper = shiftMapper;
    }

    @Override
    public List<ShiftDTO.ShiftDto> getAllShifts() {
        List<Shift> shifts = shiftRepository.findAll();
        List<ShiftDTO.ShiftDto> shiftDtos = new ArrayList<>();
        for (Shift s : shifts) {
            shiftDtos.add(shiftMapper.toDto(s));
        }
        return shiftDtos;
    }

    @Override
    public ShiftDTO.ShiftDto getShiftById(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException("Shift not found with id: " + shiftId));

        return shiftMapper.toDto(shift);
    }

    @Override
    public ShiftAssignmentDTO.ShiftAssignmentDto assignEmployeeToShift(Long shiftId, Long employeeId) {
        Shift shift = findShiftById(shiftId);
        Employee employee = findEmployeeById(employeeId);

        validateAssignment(shift, employee);
        shift.getEmployees().add(employee);

        Shift saved = shiftRepository.save(shift);
        return shiftMapper.toDto(saved, employee);
    }

    @Override
    public void unassignEmployeeFromShift(Long shiftId, Long employeeId) {
        Shift shift = findShiftById(shiftId);
        Employee employee = findEmployeeById(employeeId);

        boolean removed = shift.getEmployees().remove(employee);
        if (!removed) {
            throw new EmployeeNotAssignedException("Employee is not assigned to this shift");
        }

        employee.getShifts().remove(shift);
        shiftRepository.save(shift);
    }

    // ----- Helper Methods -----

    private Shift findShiftById(Long shiftId) {
        return shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException("Shift not found with id: " + shiftId));
    }

    private Employee findEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));
    }

    private void validateAssignment(Shift shift, Employee employee) {

        // Duplicate assignment check
        for (Employee e : shift.getEmployees()) {
            if (e.getId() == employee.getId()) {
                throw new EmployeeAlreadyAssignedException("Employee already assigned to this shift");
            }
        }

        // Overlapping shift check
        for (Shift s : employee.getShifts()) {
            boolean overlaps = s.getStartTime().isBefore(shift.getEndTime())
                    && s.getEndTime().isAfter(shift.getStartTime());
            if (overlaps) {
                throw new OverlappingShiftException("Employee already assigned to an overlapping shift");
            }
        }
    }

}
