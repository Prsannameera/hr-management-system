# HR Management System Architecture

## Overview

This HRMS is a full-stack web application.

```text
Browser
  |
  | React UI
  v
Spring Boot REST API
  |
  | Service layer
  v
Spring Data JPA repositories
  |
  v
MySQL database
```

## Technologies Used

### Frontend

- React.js
- Vite
- React Router
- Lucide React icons
- CSS

### Backend

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Maven

### Database

- MySQL
- Hibernate ORM

### Development Tools

- IntelliJ IDEA
- MySQL Workbench
- Node.js and npm

## Main Modules

### Authentication

Files:

- `UserAccount.java`
- `UserAccountRepository.java`
- `UserAccountService.java`
- `AuthApiController.java`

Purpose:

- Signup
- Login
- Logout
- Session check

### Employee Management

Files:

- `Employee.java`
- `EmployeeRepository.java`
- `EmployeeService.java`
- `EmployeeApiController.java`

Purpose:

- Add employee
- View employee
- Search employee
- Update employee
- Delete employee

### Attendance

Files:

- `AttendanceRecord.java`
- `AttendanceRecordRepository.java`
- `AttendanceService.java`
- `AttendanceApiController.java`

Purpose:

- Mark present/absent
- Count present today
- Count absent today

### Leave Management

Files:

- `LeaveRequest.java`
- `LeaveRequestRepository.java`
- `LeaveService.java`
- `LeaveApiController.java`

Purpose:

- Submit leave request
- Approve leave
- Reject leave
- Update employee leave status

### Payroll

Files:

- `PayrollSummary.java`
- `PayrollService.java`
- `PayrollApiController.java`

Purpose:

- Calculate salary
- Calculate deductions
- Show net salary

### Hiring

Files:

- `JobOpening.java`
- `JobOpeningRepository.java`
- `RecruitmentService.java`
- `RecruitmentApiController.java`

Purpose:

- Create job opening
- Close job opening
- Show open positions

### Settings

Files:

- `CompanySettings.java`
- `CompanySettingsRepository.java`
- `SettingsService.java`
- `SettingsApiController.java`

Purpose:

- Company name
- HR manager name
- Office location
- Annual leave days

## Frontend Structure

```text
frontend/
  package.json
  vite.config.js
  index.html
  src/
    main.jsx
    styles.css
```

React is built into:

```text
src/main/resources/static/
```

That lets Spring Boot serve both frontend and backend from:

```text
http://localhost:8080
```

## Backend Structure

```text
src/main/java/com/example/hrms/
  HrManagementSystemApplication.java
  config/
  controller/
    ReactAppController.java
    api/
  model/
  repository/
  service/
```

## API Pattern

React calls backend endpoints like:

```text
/api/auth/login
/api/employees
/api/attendance
/api/leave
/api/payroll
/api/jobs
/api/reports
/api/settings
```

## Data Storage

Data is stored in MySQL tables created automatically by Spring Boot/JPA.

Important setting:

```properties
spring.jpa.hibernate.ddl-auto=update
```

This creates/updates tables automatically when the application starts.
