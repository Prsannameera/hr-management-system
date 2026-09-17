package com.example.hrms.controller.api;

import com.example.hrms.model.PayrollSummary;
import com.example.hrms.service.EmployeeService;
import com.example.hrms.service.PayrollService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.Map;

@RestController
@RequestMapping("/api/payroll")
public class PayrollApiController {
    private final EmployeeService employeeService;
    private final PayrollService payrollService;

    public PayrollApiController(EmployeeService employeeService, PayrollService payrollService) {
        this.employeeService = employeeService;
        this.payrollService = payrollService;
    }

    @GetMapping
    public Map<String, Object> payroll(@RequestParam(required = false) Integer employeeId, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        YearMonth selectedMonth = month == null ? YearMonth.now() : month;
        PayrollSummary summary = null;
        if (employeeId != null) {
            summary = employeeService.findById(employeeId)
                    .map(employee -> payrollService.generatePayroll(employee, selectedMonth))
                    .orElse(null);
        }
        return Map.of(
                "employees", employeeService.getAllEmployees(),
                "selectedMonth", selectedMonth,
                "summary", summary == null ? "" : summary
        );
    }
}
