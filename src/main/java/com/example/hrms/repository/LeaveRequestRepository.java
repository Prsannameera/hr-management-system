package com.example.hrms.repository;

import com.example.hrms.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
}
