package com.example.hrms.repository;

import com.example.hrms.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Integer> {
    Optional<AttendanceRecord> findByEmployeeIdAndDate(int employeeId, LocalDate date);
}
