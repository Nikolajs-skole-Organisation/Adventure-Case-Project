package com.example.backend.service;

import com.example.backend.dto.ShiftAssignmentDTO;
import com.example.backend.dto.ShiftMapper;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Employee;
import com.example.backend.model.Shift;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.ShiftRepository;
import org.springframework.stereotype.Service;

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
    public ShiftAssignmentDTO.ShiftAssignmentDto assignEmployeeToShift(Long shiftId, Long employeeId) {

        // Checks if shift is found
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift not found with id: " + shiftId));

        // Checks if employee is found
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Employee not found with id:" + employeeId));

        // Checks if employee is already assigned to this shift (prevents duplicates)
        for (Employee e : shift.getEmployees()) {
            if (e.getId() == employeeId) {
                throw new IllegalArgumentException("Employee already assigned to this shift");
            }
        }

        // Checks if shift overlaps with another shift for this employee (prevents overlaps)
        for (Shift s : employee.getShifts()) {
            boolean overlaps = s.getStartTime().isBefore(shift.getEndTime())
                    && s.getEndTime().isAfter(shift.getStartTime());
            if (overlaps) {
                throw new IllegalStateException("Employee already assigned to an overlapping shift");
            }
        }

        // Assigns this employee to this shift
        shift.getEmployees().add(employee);
        Shift saved = shiftRepository.save(shift);

        // Maps to DTO
        return shiftMapper.toDto(saved, employee);
    }
}
