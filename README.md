# HR Management System

A Spring Boot + React HR Management System with MySQL persistence.

## Features

- React.js frontend with Vite
- Spring Boot REST API backend
- MySQL database persistence with Spring Data JPA
- Login and signup with Spring Security session authentication
- Dashboard with workforce and payroll metrics
- Add, view, search, update, and remove employees
- Manage department, role, salary, and joining date
- Mark daily attendance
- Generate monthly payroll summaries
- Submit, approve, and reject leave requests
- Create and close hiring/open position records
- Reports page with printable workforce summary
- Company settings page
- Responsive professional web UI

## Open in IntelliJ

1. Open IntelliJ IDEA.
2. Choose **File > Open**.
3. Select this folder: `hr-management-system`.
4. Wait for Maven indexing to finish.
5. Make sure MySQL is running.
6. Open `src/main/resources/application.properties`.
7. Update `spring.datasource.username` and `spring.datasource.password` if your MySQL login is different.
8. Open `src/main/java/com/example/hrms/HrManagementSystemApplication.java`.
9. Click the green run button beside `main`.
10. Open a terminal in the `frontend` folder.
11. Run `npm install` once.
12. Run `npm run build`.
13. Open the app at `http://localhost:8080`.

For development, you can still run `npm run dev` and open `http://127.0.0.1:5173`.

## MySQL Setup

The app uses this database:

```properties
hrms_db
```

The connection URL includes `createDatabaseIfNotExist=true`, so Spring Boot can create the database automatically if your MySQL user has permission.

You can also create it manually in MySQL Workbench:

```sql
CREATE DATABASE hrms_db;
```

Spring Boot creates and updates the tables automatically using:

```properties
spring.jpa.hibernate.ddl-auto=update
```

## Login

Default account created on first run:

```text
Email: admin@hrms.com
Password: admin123
```

You can also create a new account from the signup page.

## Run from Terminal

```bash
mvn spring-boot:run
```

Then in a second terminal:

```bash
cd frontend
npm install
npm run build
```

## Project Structure

```text
src/main/java/com/example/hrms/
  HrManagementSystemApplication.java
  config/
  controller/
  model/
  service/
src/main/resources/
  templates/
  static/css/
```

## Main Pages

- `http://localhost:8080/` - React dashboard served by Spring Boot
- `http://localhost:8080/login` - React login
- `http://localhost:8080/signup` - React signup
- `http://localhost:8080/api/...` - Spring Boot REST API

The old Thymeleaf pages are still present as fallback/reference, but the upgraded frontend is now in the `frontend` folder.
