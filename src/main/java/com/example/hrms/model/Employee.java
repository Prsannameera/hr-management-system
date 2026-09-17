package com.example.hrms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "employees")
public class Employee {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String department;
    private String role;
    private double monthlySalary;
    private LocalDate joiningDate;
    private LocalDate birthDate;
    private String status;

    public Employee() {
    }

    public Employee(String name, String email, String department, String role, double monthlySalary, LocalDate joiningDate, LocalDate birthDate, String status) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
        this.monthlySalary = monthlySalary;
        this.joiningDate = joiningDate;
        this.birthDate = birthDate;
        this.status = status;
    }

    public Employee(int id, String name, String email, String department, String role, double monthlySalary, LocalDate joiningDate) {
        this(id, name, email, department, role, monthlySalary, joiningDate, joiningDate.plusYears(25), "Active");
    }

    public Employee(int id, String name, String email, String department, String role, double monthlySalary, LocalDate joiningDate, LocalDate birthDate, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
        this.monthlySalary = monthlySalary;
        this.joiningDate = joiningDate;
        this.birthDate = birthDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | %s | %s | %s | %s | Salary: %.2f | Joined: %s",
                id,
                name,
                email,
                department,
                role,
                monthlySalary,
                joiningDate.format(DATE_FORMAT)
        );
    }
}
