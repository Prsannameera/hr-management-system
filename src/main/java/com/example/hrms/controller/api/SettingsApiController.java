package com.example.hrms.controller.api;

import com.example.hrms.model.CompanySettings;
import com.example.hrms.service.SettingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingsApiController {
    private final SettingsService settingsService;

    public SettingsApiController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public CompanySettings settings() {
        return settingsService.getSettings();
    }

    @PutMapping
    public CompanySettings update(@RequestBody CompanySettings request) {
        settingsService.update(request.getCompanyName(), request.getHrManager(), request.getOfficeLocation(), request.getAnnualLeaveDays());
        return settingsService.getSettings();
    }
}
