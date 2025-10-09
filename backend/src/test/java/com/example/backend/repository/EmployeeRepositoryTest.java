package com.example.backend.repository;

import com.example.backend.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee makeEmployee(String name, String email, String phone, String role, String password) {
        return new Employee(null, name, email, phone, role, password);
    }

    @Test
    void findByEmailAndPassword_returnsEmployee_whenValid() {
        Employee alice = makeEmployee("Alice Anders", "alice@gokart.com", "12345678", "Employee", "velkommen123");
        employeeRepository.save(alice);

        Optional<Employee> result = employeeRepository.findByEmailAndPassword("alice@gokart.com", "velkommen123");

        assertTrue(result.isPresent());
        assertEquals("Alice Anders", result.get().getName());
        assertNotNull(result.get().getId());
    }

    @Test
    void findByEmailAndPassword_returnsEmpty_whenWrongPassword() {
        Employee alice = makeEmployee("Alice Anders", "alice@gokart.com", "12345678", "Employee", "velkommen123");
        employeeRepository.save(alice);

        Optional<Employee> result = employeeRepository.findByEmailAndPassword("alice@gokart.com", "forkert");

        assertTrue(result.isEmpty());
    }

    @Test
    void uniqueEmail_isEnforced() {
        Employee e1 = makeEmployee("En", "unique@example.com", "11111111", "Employee", "pw1");
        Employee e2 = makeEmployee("To",  "unique@example.com", "22222222", "Employee", "pw2");

        employeeRepository.saveAndFlush(e1);

        assertThrows(DataIntegrityViolationException.class, () -> {
            employeeRepository.saveAndFlush(e2);
        });
    }
}