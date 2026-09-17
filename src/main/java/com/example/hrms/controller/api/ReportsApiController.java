package com.example.hrms.controller.api;

import com.example.hrms.service.EmployeeService;
import com.example.hrms.service.LeaveService;
import com.example.hrms.service.RecruitmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportsApiController {
    private final EmployeeService employeeService;
    private final LeaveService leaveService;
    private final RecruitmentService recruitmentService;

    public ReportsApiController(EmployeeService employeeService, LeaveService leaveService, RecruitmentService recruitmentService) {
        this.employeeService = employeeService;
        this.leaveService = leaveService;
        this.recruitmentService = recruitmentService;
    }

    @GetMapping
    public Map<String, Object> reports() {
        return Map.of(
                "employees", employeeService.getAllEmployees(),
                "employeeCount", employeeService.getAllEmployees().size(),
                "activeEmployees", employeeService.countByStatus("Active"),
                "onLeaveEmployees", employeeService.countByStatus("On Leave"),
                "pendingLeaves", leaveService.countPending(),
                "openPositions", recruitmentService.countOpenPositions(),
                "totalPayroll", employeeService.totalMonthlyPayroll()
        );
    }
}
