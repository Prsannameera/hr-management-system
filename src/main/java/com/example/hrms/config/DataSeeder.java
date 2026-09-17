package com.example.hrms.config;

import com.example.hrms.model.Employee;
import com.example.hrms.service.AttendanceService;
import com.example.hrms.service.EmployeeService;
import com.example.hrms.service.LeaveService;
import com.example.hrms.service.RecruitmentService;
import com.example.hrms.service.UserAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final LeaveService leaveService;
    private final RecruitmentService recruitmentService;
    private final UserAccountService userAccountService;

    public DataSeeder(EmployeeService employeeService, AttendanceService attendanceService, LeaveService leaveService, RecruitmentService recruitmentService, UserAccountService userAccountService) {
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
        this.leaveService = leaveService;
        this.recruitmentService = recruitmentService;
        this.userAccountService = userAccountService;
    }

    @Override
    public void run(String... args) {
        if (!userAccountService.hasUsers()) {
            userAccountService.register("HR Manager", "admin@hrms.com", "admin123");
        }

        if (!employeeService.getAllEmployees().isEmpty()) {
            return;
        }

        Employee ananya = employeeService.addEmployee("Ananya Sharma", "ananya.sharma@example.com", "Human Resources", "HR Executive", 52000, LocalDate.of(2024, 4, 15), LocalDate.of(1996, 7, 22), "Active");
        Employee rahul = employeeService.addEmployee("Rahul Mehta", "rahul.mehta@example.com", "Engineering", "Software Engineer", 85000, LocalDate.of(2023, 8, 1), LocalDate.of(1994, 8, 10), "Active");
        Employee meera = employeeService.addEmployee("Meera Iyer", "meera.iyer@example.com", "Finance", "Payroll Specialist", 64000, LocalDate.of(2022, 11, 7), LocalDate.of(1993, 7, 30), "On Leave");

        LocalDate today = LocalDate.now();
        attendanceService.markAttendance(ananya.getId(), today, true);
        attendanceService.markAttendance(rahul.getId(), today, true);
        attendanceService.markAttendance(meera.getId(), today, false);

        leaveService.requestLeave(meera, "Medical Leave", today, today.plusDays(2), "Doctor appointment and recovery");
        leaveService.requestLeave(rahul, "Casual Leave", today.plusDays(5), today.plusDays(5), "Family work");

        recruitmentService.addOpening("Backend Developer", "Engineering", "Bengaluru", 2);
        recruitmentService.addOpening("HR Coordinator", "Human Resources", "Mumbai", 1);
        recruitmentService.addOpening("Accounts Executive", "Finance", "Pune", 1);
    }
}
