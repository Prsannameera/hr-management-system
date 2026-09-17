package com.example.hrms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "attendance_records")
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int employeeId;
    @Column(name = "attendance_date")
    private LocalDate date;
    private boolean present;

    public AttendanceRecord() {
    }

    public AttendanceRecord(int employeeId, LocalDate date, boolean present) {
        this.employeeId = employeeId;
        this.date = date;
        this.present = present;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isPresent() {
        return present;
    }
}
