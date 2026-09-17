package com.example.hrms.controller.api;

import com.example.hrms.service.AttendanceService;
import com.example.hrms.service.EmployeeService;
import com.example.hrms.service.LeaveService;
import com.example.hrms.service.RecruitmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final LeaveService leaveService;
    private final RecruitmentService recruitmentService;

    public DashboardApiController(EmployeeService employeeService, AttendanceService attendanceService, LeaveService leaveService, RecruitmentService recruitmentService) {
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
        this.leaveService = leaveService;
        this.recruitmentService = recruitmentService;
    }

    @GetMapping
    public Map<String, Object> dashboard() {
        LocalDate today = LocalDate.now();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("employees", employeeService.getAllEmployees());
        response.put("employeeCount", employeeService.getAllEmployees().size());
        response.put("departmentCount", employeeService.countDepartments());
        response.put("averageSalary", employeeService.averageSalary());
        response.put("totalPayroll", employeeService.totalMonthlyPayroll());
        response.put("presentToday", attendanceService.countPresentOn(today));
        response.put("absentToday", attendanceService.countAbsentOn(today));
        response.put("openPositions", recruitmentService.countOpenPositions());
        response.put("pendingLeaves", leaveService.countPending());
        response.put("birthdays", employeeService.upcomingBirthdays());
        response.put("leaveRequests", leaveService.pendingRequests());
        response.put("jobOpenings", recruitmentService.getAllOpenings());
        return response;
    }
}
