package com.example.hrms.controller.api;

import com.example.hrms.model.JobOpening;
import com.example.hrms.service.RecruitmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class RecruitmentApiController {
    private final RecruitmentService recruitmentService;

    public RecruitmentApiController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @GetMapping
    public List<JobOpening> jobs() {
        return recruitmentService.getAllOpenings();
    }

    @PostMapping
    public JobOpening addJob(@RequestBody JobRequest request) {
        return recruitmentService.addOpening(request.title(), request.department(), request.location(), request.openings());
    }

    @PostMapping("/{id}/close")
    public Map<String, Object> closeJob(@PathVariable int id) {
        recruitmentService.closeOpening(id);
        return Map.of("closed", true);
    }

    public record JobRequest(String title, String department, String location, int openings) {
    }
}
