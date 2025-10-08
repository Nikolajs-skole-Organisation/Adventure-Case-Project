package com.example.backend.integrationTests;

import com.example.backend.controller.EmployeeController;
import com.example.backend.dto.EmployeeDTO;
import com.example.backend.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void login_returns200_andEmployeeDto_whenCredentialsAreValid() throws Exception {
        var dto = new EmployeeDTO.EmployeeDto(
                1L,
                "Alice Anders",
                "alice@gokart.com",
                "12345678"
        );

        when(employeeService.login(eq("alice@gokart.com"), eq("velkommen123")))
                .thenReturn(dto);

        String body = """
            {
              "email": "alice@gokart.com",
              "password": "velkommen123"
            }
            """;

        mockMvc.perform(post("/api/employees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice Anders"))
                .andExpect(jsonPath("$.email").value("alice@gokart.com"))
                .andExpect(jsonPath("$.phone").value("12345678"));
    }

    @Test
    void login_returns401_whenPasswordIsWrong() throws Exception {
        when(employeeService.login(eq("alice@gokart.com"), eq("forkert")))
                .thenThrow(new IllegalArgumentException("Invalid email or password"));

        String body = """
            {
              "email": "alice@gokart.com",
              "password": "forkert"
            }
            """;

        mockMvc.perform(post("/api/employees/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid email or password")));
    }
}