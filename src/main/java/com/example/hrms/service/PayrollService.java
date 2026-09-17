package com.example.hrms.service;

import com.example.hrms.model.Employee;
import com.example.hrms.model.PayrollSummary;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
public class PayrollService {
    private final AttendanceService attendanceService;

    public PayrollService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    public PayrollSummary generatePayroll(Employee employee, YearMonth month) {
        int workingDays = attendanceService.countWorkingDays(month);
        int presentDays = attendanceService.countPresentDays(employee.getId(), month);

        double grossSalary = employee.getMonthlySalary();
        double dailySalary = grossSalary / workingDays;
        double deductions = dailySalary * Math.max(workingDays - presentDays, 0);
        double netSalary = grossSalary - deductions;

        return new PayrollSummary(employee, month, workingDays, presentDays, grossSalary, deductions, netSalary);
    }
}
