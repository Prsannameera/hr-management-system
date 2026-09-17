package com.example.hrms.controller.api;

import com.example.hrms.service.AttendanceService;
import com.example.hrms.service.EmployeeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceApiController {
    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    public AttendanceApiController(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public Map<String, Object> attendance() {
        LocalDate today = LocalDate.now();
        return Map.of(
                "employees", employeeService.getAllEmployees(),
                "today", today,
                "presentToday", attendanceService.countPresentOn(today),
                "absentToday", attendanceService.countAbsentOn(today)
        );
    }

    @PostMapping
    public Map<String, Object> markAttendance(@RequestBody AttendanceRequest request) {
        attendanceService.markAttendance(request.employeeId(), request.date(), request.present());
        if (request.date().equals(LocalDate.now())) {
            employeeService.updateStatus(request.employeeId(), request.present() ? "Active" : "Absent");
        }
        return Map.of("saved", true);
    }

    public record AttendanceRequest(int employeeId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, boolean present) {
    }
}
