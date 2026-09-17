package com.example.hrms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_openings")
public class JobOpening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String department;
    private String location;
    private int openings;
    private String status;

    public JobOpening() {
    }

    public JobOpening(String title, String department, String location, int openings, String status) {
        this.title = title;
        this.department = department;
        this.location = location;
        this.openings = openings;
        this.status = status;
    }

    public JobOpening(int id, String title, String department, String location, int openings, String status) {
        this.id = id;
        this.title = title;
        this.department = department;
        this.location = location;
        this.openings = openings;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOpenings() {
        return openings;
    }

    public void setOpenings(int openings) {
        this.openings = openings;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
