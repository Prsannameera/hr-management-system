package com.example.hrms.service;

import com.example.hrms.model.AttendanceRecord;
import com.example.hrms.repository.AttendanceRecordRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class AttendanceService {
    private final AttendanceRecordRepository attendanceRecordRepository;

    public AttendanceService(AttendanceRecordRepository attendanceRecordRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    public void markAttendance(int employeeId, LocalDate date, boolean present) {
        attendanceRecordRepository.findByEmployeeIdAndDate(employeeId, date)
                .ifPresent(attendanceRecordRepository::delete);
        attendanceRecordRepository.save(new AttendanceRecord(employeeId, date, present));
    }

    public int countPresentDays(int employeeId, YearMonth month) {
        return (int) attendanceRecordRepository.findAll().stream()
                .filter(record -> record.getEmployeeId() == employeeId)
                .filter(record -> YearMonth.from(record.getDate()).equals(month))
                .filter(AttendanceRecord::isPresent)
                .count();
    }

    public int countPresentOn(LocalDate date) {
        return (int) attendanceRecordRepository.findAll().stream()
                .filter(record -> record.getDate().equals(date))
                .filter(AttendanceRecord::isPresent)
                .count();
    }

    public int countAbsentOn(LocalDate date) {
        return (int) attendanceRecordRepository.findAll().stream()
                .filter(record -> record.getDate().equals(date))
                .filter(record -> !record.isPresent())
                .count();
    }

    public int countWorkingDays(YearMonth month) {
        int workingDays = 0;
        for (int day = 1; day <= month.lengthOfMonth(); day++) {
            LocalDate date = month.atDay(day);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays++;
            }
        }
        return workingDays;
    }
}
