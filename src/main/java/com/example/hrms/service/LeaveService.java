package com.example.hrms.service;

import com.example.hrms.model.Employee;
import com.example.hrms.model.LeaveRequest;
import com.example.hrms.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {
    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public LeaveRequest requestLeave(Employee employee, String leaveType, LocalDate startDate, LocalDate endDate, String reason) {
        LeaveRequest request = new LeaveRequest(employee.getId(), employee.getName(), leaveType, startDate, endDate, reason, "Pending");
        return leaveRequestRepository.save(request);
    }

    public List<LeaveRequest> getAllRequests() {
        return leaveRequestRepository.findAll().stream()
                .sorted(Comparator.comparing(LeaveRequest::getStartDate).reversed())
                .toList();
    }

    public List<LeaveRequest> pendingRequests() {
        return leaveRequestRepository.findAll().stream()
                .filter(request -> request.getStatus().equals("Pending"))
                .toList();
    }

    public Optional<LeaveRequest> updateStatus(int id, String status) {
        Optional<LeaveRequest> leaveRequest = leaveRequestRepository.findById(id);
        leaveRequest.ifPresent(request -> {
            request.setStatus(status);
            leaveRequestRepository.save(request);
        });
        return leaveRequest;
    }

    public long countPending() {
        return pendingRequests().size();
    }
}
