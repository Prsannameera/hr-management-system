package com.example.hrms.service;

import com.example.hrms.model.CompanySettings;
import com.example.hrms.repository.CompanySettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    private final CompanySettingsRepository companySettingsRepository;

    public SettingsService(CompanySettingsRepository companySettingsRepository) {
        this.companySettingsRepository = companySettingsRepository;
    }

    public CompanySettings getSettings() {
        return companySettingsRepository.findById(1L).orElseGet(() -> companySettingsRepository.save(new CompanySettings()));
    }

    public void update(String companyName, String hrManager, String officeLocation, int annualLeaveDays) {
        CompanySettings settings = getSettings();
        settings.setCompanyName(companyName);
        settings.setHrManager(hrManager);
        settings.setOfficeLocation(officeLocation);
        settings.setAnnualLeaveDays(annualLeaveDays);
        companySettingsRepository.save(settings);
    }
}
