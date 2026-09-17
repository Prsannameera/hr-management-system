package com.example.hrms.controller.api;

import com.example.hrms.model.Employee;
import com.example.hrms.service.EmployeeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeApiController {
    private final EmployeeService employeeService;

    public EmployeeApiController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> employees(@RequestParam(defaultValue = "") String search) {
        return search.isBlank() ? employeeService.getAllEmployees() : employeeService.search(search);
    }

    @PostMapping
    public Employee addEmployee(@RequestBody EmployeeRequest request) {
        return employeeService.addEmployee(
                request.name(),
                request.email(),
                request.department(),
                request.role(),
                request.monthlySalary(),
                request.joiningDate(),
                request.birthDate(),
                "Active"
        );
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateEmployee(@PathVariable int id, @RequestBody EmployeeUpdateRequest request) {
        boolean updated = employeeService.updateEmployee(id, request.department(), request.role(), request.monthlySalary(), request.status());
        return Map.of("updated", updated);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteEmployee(@PathVariable int id) {
        return Map.of("deleted", employeeService.removeEmployee(id));
    }

    public record EmployeeRequest(
            String name,
            String email,
            String department,
            String role,
            double monthlySalary,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate joiningDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate
    ) {
    }

    public record EmployeeUpdateRequest(String department, String role, double monthlySalary, String status) {
    }
}
