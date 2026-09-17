package com.example.hrms.service;

import com.example.hrms.model.JobOpening;
import com.example.hrms.repository.JobOpeningRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RecruitmentService {
    private final JobOpeningRepository jobOpeningRepository;

    public RecruitmentService(JobOpeningRepository jobOpeningRepository) {
        this.jobOpeningRepository = jobOpeningRepository;
    }

    public JobOpening addOpening(String title, String department, String location, int openings) {
        return jobOpeningRepository.save(new JobOpening(title, department, location, openings, "Open"));
    }

    public List<JobOpening> getAllOpenings() {
        return jobOpeningRepository.findAll().stream()
                .sorted(Comparator.comparing(JobOpening::getId))
                .toList();
    }

    public void closeOpening(int id) {
        jobOpeningRepository.findById(id).ifPresent(opening -> {
            opening.setStatus("Closed");
            jobOpeningRepository.save(opening);
        });
    }

    public int countOpenPositions() {
        return jobOpeningRepository.findAll().stream()
                .filter(opening -> opening.getStatus().equals("Open"))
                .mapToInt(JobOpening::getOpenings)
                .sum();
    }
}
