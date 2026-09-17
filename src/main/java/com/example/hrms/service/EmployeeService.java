package com.example.hrms.service;

import com.example.hrms.model.Employee;
import com.example.hrms.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee addEmployee(String name, String email, String department, String role, double monthlySalary, LocalDate joiningDate) {
        return addEmployee(name, email, department, role, monthlySalary, joiningDate, joiningDate.plusYears(25), "Active");
    }

    public Employee addEmployee(String name, String email, String department, String role, double monthlySalary, LocalDate joiningDate, LocalDate birthDate, String status) {
        return employeeRepository.save(new Employee(name, email, department, role, monthlySalary, joiningDate, birthDate, status));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Employee::getId))
                .toList();
    }

    public Optional<Employee> findById(int id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> search(String keyword) {
        String normalizedKeyword = keyword.toLowerCase();
        return employeeRepository.findAll().stream()
                .filter(employee -> matches(employee, normalizedKeyword))
                .sorted(Comparator.comparingInt(Employee::getId))
                .toList();
    }

    public boolean updateEmployee(int id, String department, String role, double monthlySalary) {
        return updateEmployee(id, department, role, monthlySalary, "Active");
    }

    public boolean updateEmployee(int id, String department, String role, double monthlySalary, String status) {
        Optional<Employee> employee = findById(id);
        employee.ifPresent(current -> {
            current.setDepartment(department);
            current.setRole(role);
            current.setMonthlySalary(monthlySalary);
            current.setStatus(status);
            employeeRepository.save(current);
        });
        return employee.isPresent();
    }

    public boolean updateStatus(int id, String status) {
        Optional<Employee> employee = findById(id);
        employee.ifPresent(current -> {
            current.setStatus(status);
            employeeRepository.save(current);
        });
        return employee.isPresent();
    }

    public boolean removeEmployee(int id) {
        if (!employeeRepository.existsById(id)) {
            return false;
        }
        employeeRepository.deleteById(id);
        return true;
    }

    public long countDepartments() {
        return employeeRepository.findAll().stream()
                .map(Employee::getDepartment)
                .distinct()
                .count();
    }

    public double averageSalary() {
        return employeeRepository.findAll().stream()
                .mapToDouble(Employee::getMonthlySalary)
                .average()
                .orElse(0);
    }

    public double totalMonthlyPayroll() {
        return employeeRepository.findAll().stream()
                .mapToDouble(Employee::getMonthlySalary)
                .sum();
    }

    public List<Employee> upcomingBirthdays() {
        return employeeRepository.findAll().stream()
                .sorted(Comparator.comparing(employee -> employee.getBirthDate().getDayOfYear()))
                .limit(4)
                .toList();
    }

    public long countByStatus(String status) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getStatus().equalsIgnoreCase(status))
                .count();
    }

    private boolean matches(Employee employee, String keyword) {
        return employee.getName().toLowerCase().contains(keyword)
                || employee.getEmail().toLowerCase().contains(keyword)
                || employee.getDepartment().toLowerCase().contains(keyword)
                || employee.getRole().toLowerCase().contains(keyword)
                || employee.getStatus().toLowerCase().contains(keyword);
    }
}
