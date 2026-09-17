# Deployment Guide

## Recommended Deployment

Use Railway for the simplest deployment because it supports Spring Boot and MySQL in one project.

Official Railway Spring Boot guide:

```text
https://docs.railway.com/guides/spring-boot
```

Official Railway MySQL guide:

```text
https://docs.railway.com/databases/mysql
```

## Before Uploading To GitHub

Do not upload generated folders:

```text
target/
frontend/node_modules/
frontend/dist/
.idea/
```

They are already listed in `.gitignore`.

## Build Frontend

From the project root:

```bash
cd frontend
npm install
npm run build
```

This puts the React build into:

```text
src/main/resources/static/
```

## Run Locally

Start MySQL, then run Spring Boot from IntelliJ.

Open:

```text
http://localhost:8080
```

## Environment Variables For Deployment

Set these in the hosting platform:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
PORT
```

Example MySQL URL:

```text
jdbc:mysql://HOST:PORT/DATABASE?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
```

## Deploy On Railway

1. Push this project to GitHub.
2. Go to Railway.
3. Create a new project.
4. Add a MySQL database.
5. Add a service from your GitHub repository.
6. Set the environment variables:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
```

7. Deploy the Spring Boot service.
8. In Railway service settings, generate a public domain.
9. Share that public domain with your team.

## Login

Default login created on first run:

```text
Email: admin@hrms.com
Password: admin123
```

If login does not work, create a new account from:

```text
/signup
```

## Final Public Link

After deployment, your link will look like:

```text
https://your-service-name.up.railway.app
```
