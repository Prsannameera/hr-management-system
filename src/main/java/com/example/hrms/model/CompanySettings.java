package com.example.hrms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "company_settings")
public class CompanySettings {
    @Id
    private Long id = 1L;
    private String companyName = "PeopleCore";
    private String hrManager = "HR Manager";
    private String officeLocation = "Mumbai";
    private int annualLeaveDays = 24;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHrManager() {
        return hrManager;
    }

    public void setHrManager(String hrManager) {
        this.hrManager = hrManager;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public int getAnnualLeaveDays() {
        return annualLeaveDays;
    }

    public void setAnnualLeaveDays(int annualLeaveDays) {
        this.annualLeaveDays = annualLeaveDays;
    }
}
