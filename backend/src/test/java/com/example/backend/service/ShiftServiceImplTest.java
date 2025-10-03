package com.example.backend.service;

import com.example.backend.dto.ShiftAssignmentDTO;
import com.example.backend.dto.ShiftDTO;
import com.example.backend.dto.ShiftMapper;
import com.example.backend.exception.EmployeeAlreadyAssignedException;
import com.example.backend.exception.EmployeeNotFoundException;
import com.example.backend.exception.OverlappingShiftException;
import com.example.backend.exception.ShiftNotFoundException;
import com.example.backend.model.Employee;
import com.example.backend.model.Shift;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.ShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShiftServiceImplTest {

    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ShiftMapper shiftMapper;

    @InjectMocks
    private ShiftServiceImpl service;

    private Shift shift;
    private Employee employee;

    @BeforeEach
    void setUp() {
        shift = new Shift();
        shift.setId(1L);
        shift.setStartTime(LocalDateTime.of(2025, 10, 15, 9, 0));
        shift.setEndTime(LocalDateTime.of(2025, 10, 15, 12, 0));
        shift.setEmployees(new HashSet<>());

        employee = new Employee();
        employee.setId(10L);
        employee.setShifts(new HashSet<>());
    }

    // ----- getAllShifts -----

    @Test
    void getAllShifts() {
        Shift shift2 = new Shift();
        shift2.setId(2L);

        when(shiftRepository.findAll()).thenReturn(List.of(shift, shift2));
        when(shiftMapper.toDto(shift)).thenReturn(new ShiftDTO.ShiftDto(1L, null, null, null));
        when(shiftMapper.toDto(shift2)).thenReturn(new ShiftDTO.ShiftDto(2L, null, null, null));

        List<ShiftDTO.ShiftDto> shiftDtos = service.getAllShifts();

        assertEquals(2, shiftDtos.size());
        assertEquals(1L, shiftDtos.getFirst().id());
        assertEquals(2L, shiftDtos.get(1).id());
    }

    // ----- assignEmployeeToShift (successful) -----

    @Test
    void assignEmployeeShift_successful() {
        when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));
        when(employeeRepository.findById(10L)).thenReturn(Optional.of(employee));
        when(shiftRepository.save(shift)).thenReturn(shift);

        ShiftAssignmentDTO.ShiftAssignmentDto expected = new ShiftAssignmentDTO.ShiftAssignmentDto(1L, 10L, "Name", "email@example.com", "12345678");

        when(shiftMapper.toDto(shift, employee)).thenReturn(expected);

        ShiftAssignmentDTO.ShiftAssignmentDto actual = service.assignEmployeeToShift(1L, 10L);

        assertEquals(expected, actual);
        assertTrue(shift.getEmployees().contains(employee));
    }

    // ----- assignEmployeeToShift (errors) -----

    @Test
    void assignEmployeeToShift_shiftNotFound() {
        when(shiftRepository.findById(1L)).thenReturn((Optional.empty()));

        assertThrows(ShiftNotFoundException.class,
                () -> service.assignEmployeeToShift(1L, 10L));
    }

    @Test
    void assignEmployeeToShift_employeeNotFound() {
        when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));
        when(employeeRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class,
                () -> service.assignEmployeeToShift(1L, 10L));
    }

    @Test
    void assignEmployeeToShift_duplicateAssignment () {
        Employee alreadyAssigned = new Employee();
        alreadyAssigned.setId(10L);
        shift.getEmployees().add(alreadyAssigned);

        when(shiftRepository.findById(1L)).thenReturn((Optional.of(shift)));
        when(employeeRepository.findById(10L)).thenReturn(Optional.of(employee));

        assertThrows(EmployeeAlreadyAssignedException.class,
                () -> service.assignEmployeeToShift(1L, 10L));
    }

    @Test
    void assignEmployeeToShift_overlappingShift () {
        Shift existing = new Shift();
        existing. setId(99L);
        existing.setStartTime(LocalDateTime.of(2025, 10, 15, 10 ,0));
        existing.setEndTime(LocalDateTime.of(2025, 10, 15, 11 ,0));
        employee.getShifts().add(existing);

        when(shiftRepository.findById(1L)).thenReturn(Optional.of(shift));
        when(employeeRepository.findById(10L)).thenReturn(Optional.of(employee));

        assertThrows(OverlappingShiftException.class,
                () -> service.assignEmployeeToShift(1L, 10L));
    }

}
