package com.example.hrms.model;

import java.time.YearMonth;

public class PayrollSummary {
    private final Employee employee;
    private final YearMonth month;
    private final int workingDays;
    private final int presentDays;
    private final double grossSalary;
    private final double deductions;
    private final double netSalary;

    public PayrollSummary(Employee employee, YearMonth month, int workingDays, int presentDays, double grossSalary, double deductions, double netSalary) {
        this.employee = employee;
        this.month = month;
        this.workingDays = workingDays;
        this.presentDays = presentDays;
        this.grossSalary = grossSalary;
        this.deductions = deductions;
        this.netSalary = netSalary;
    }

    public Employee getEmployee() {
        return employee;
    }

    public YearMonth getMonth() {
        return month;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public int getPresentDays() {
        return presentDays;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public double getDeductions() {
        return deductions;
    }

    public double getNetSalary() {
        return netSalary;
    }

    @Override
    public String toString() {
        return String.format(
                """
                ---- Payroll Summary ----
                Employee: %s (ID: %d)
                Month: %s
                Working days: %d
                Present days: %d
                Gross salary: %.2f
                Deductions: %.2f
                Net salary: %.2f
                """,
                employee.getName(),
                employee.getId(),
                month,
                workingDays,
                presentDays,
                grossSalary,
                deductions,
                netSalary
        );
    }
}
