package com.example.backend.service;

import com.example.backend.dto.EmployeeDTO;
import com.example.backend.dto.EmployeeMapper;
import com.example.backend.model.Employee;
import com.example.backend.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl service;

    private Employee alice;
    private Employee bob;

    @BeforeEach
    void setUp() {
        alice = new Employee();
        alice.setId(1L);
        alice.setName("Alice Anders");
        alice.setEmail("alice@gokart.com");
        alice.setPhone("12345678");

        bob = new Employee();
        bob.setId(2L);
        bob.setName("Bob Biler");
        bob.setEmail("bob@gokart.com");
        bob.setPhone("87654321");
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

    @Test
    void getAllEmployees_Successful() {
        when(employeeRepository.findAll()).thenReturn(List.of(alice, bob));
        when(employeeMapper.toDto(alice)).thenReturn(new EmployeeDTO.EmployeeDto(1L, "Alice Anders", "alice@gokart.com", "12345678"));
        when(employeeMapper.toDto(bob)).thenReturn(new EmployeeDTO.EmployeeDto(2L, " Bob Biler", "bob@gokart.com", "87654321"));

        List<EmployeeDTO.EmployeeDto> result = service.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());
    }


    @Test
    void getAllEmployees_noEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of());
        List<EmployeeDTO.EmployeeDto> result = service.getAllEmployees();
        assertTrue(result.isEmpty());
    }
}