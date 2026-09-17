package com.example.hrms.repository;

import com.example.hrms.model.CompanySettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanySettingsRepository extends JpaRepository<CompanySettings, Long> {
}
