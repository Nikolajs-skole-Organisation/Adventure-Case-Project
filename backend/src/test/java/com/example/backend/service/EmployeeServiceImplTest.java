package com.example.backend.service;

import com.example.backend.dto.EmployeeDTO;
import com.example.backend.model.Employee;
import com.example.backend.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    void login_returnsEmployeeDto_whenCredentialsAreValid() {
        String email = "alice@gokart.com";
        String password = "velkommen123";

        Employee alice = new Employee(
                1L,
                "Alice Anders",
                email,
                "12345678",
                "Employee",
                password
        );

        when(employeeRepository.findByEmailAndPassword(email, password))
                .thenReturn(Optional.of(alice));

        EmployeeDTO.EmployeeDto result = service.login(email, password);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Alice Anders", result.name());
        assertEquals(email, result.email());
        assertEquals("12345678", result.phone());

        verify(employeeRepository, times(1))
                .findByEmailAndPassword(email, password);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void login_throwsIllegalArgumentException_whenCredentialsAreInvalid() {
        String email = "alice@gokart.com";
        String wrongPassword = "kodeord";

        when(employeeRepository.findByEmailAndPassword(email, wrongPassword))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.login(email, wrongPassword)
        );

        assertEquals("Invalid email or password", ex.getMessage());

        verify(employeeRepository, times(1))
                .findByEmailAndPassword(email, wrongPassword);
        verifyNoMoreInteractions(employeeRepository);
    }
}