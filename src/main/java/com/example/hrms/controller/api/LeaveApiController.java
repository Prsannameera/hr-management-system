package com.example.hrms.controller.api;

import com.example.hrms.model.LeaveRequest;
import com.example.hrms.service.EmployeeService;
import com.example.hrms.service.LeaveService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leave")
public class LeaveApiController {
    private final LeaveService leaveService;
    private final EmployeeService employeeService;

    public LeaveApiController(LeaveService leaveService, EmployeeService employeeService) {
        this.leaveService = leaveService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public Map<String, Object> leave() {
        return Map.of(
                "employees", employeeService.getAllEmployees(),
                "leaveRequests", leaveService.getAllRequests()
        );
    }

    @GetMapping("/pending")
    public List<LeaveRequest> pending() {
        return leaveService.pendingRequests();
    }

    @PostMapping
    public Map<String, Object> requestLeave(@RequestBody LeaveRequestBody request) {
        employeeService.findById(request.employeeId())
                .ifPresent(employee -> leaveService.requestLeave(employee, request.leaveType(), request.startDate(), request.endDate(), request.reason()));
        return Map.of("saved", true);
    }

    @PostMapping("/{id}/approve")
    public Map<String, Object> approve(@PathVariable int id) {
        leaveService.updateStatus(id, "Approved")
                .ifPresent(request -> employeeService.updateStatus(request.getEmployeeId(), "On Leave"));
        return Map.of("updated", true);
    }

    @PostMapping("/{id}/reject")
    public Map<String, Object> reject(@PathVariable int id) {
        leaveService.updateStatus(id, "Rejected")
                .ifPresent(request -> employeeService.updateStatus(request.getEmployeeId(), "Active"));
        return Map.of("updated", true);
    }

    public record LeaveRequestBody(
            int employeeId,
            String leaveType,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            String reason
    ) {
    }
}
