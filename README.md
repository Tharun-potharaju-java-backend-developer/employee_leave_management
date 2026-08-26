# Enterprise Leave & Attendance Management System

## Overview

A production-style Leave & Attendance Management System built with Java
and Spring Boot. The project demonstrates enterprise backend development
practices including authentication, role-based authorization, approval
workflows, caching, audit logging, testing, and containerized
deployment.

## Tech Stack

-   Java 21
-   Spring Boot 4.1.0
-   Spring Security (JWT + Refresh Tokens)
-   Spring Data JPA (Hibernate)
-   MySQL / PostgreSQL
-   Redis
-   Docker & Docker Compose
-   Swagger / OpenAPI
-   JUnit 5 & Mockito
-   Maven
-   React (Optional Frontend)

------------------------------------------------------------------------

# Features

## Authentication

-   Login
-   Register
-   Email Verification
-   Forgot Password
-   Reset Password
-   Refresh Tokens
-   JWT Authentication
-   Role-Based Access Control

## Employee

-   Dashboard
-   Profile
-   Upload Documents
-   View Leave Balance
-   Attendance History
-   Notifications

## Leave Management

-   Apply Leave
-   Leave History
-   Cancel Leave
-   Leave Approval
-   Leave Rejection
-   Comments
-   Leave Types

## Attendance

-   Check-In
-   Check-Out
-   Daily Attendance
-   Monthly Attendance
-   Attendance Reports

## HR & Admin

-   Employee Management
-   Holiday Calendar
-   Department Management
-   Role Management
-   Audit Logs
-   System Settings

## Reports

-   Attendance Analytics
-   Leave Analytics
-   Employee Reports
-   Export to Excel/PDF

------------------------------------------------------------------------

# Suggested Folder Structure

``` text
backend/
 ├── config
 ├── controller
 ├── dto
 ├── entity
 ├── repository
 ├── service
 ├── security
 ├── exception
 ├── scheduler
 ├── cache
 └── util

frontend/
 ├── components
 ├── pages
 ├── services
 ├── layouts
 ├── hooks
 └── assets
```

# Database Tables

-   users
-   roles
-   user_roles
-   employees
-   leave_requests
-   leave_types
-   attendance
-   holidays
-   notifications
-   audit_logs
-   password_reset_tokens

# Security

-   BCrypt Password Encoding
-   JWT Authentication
-   Stateless JWT Access and Refresh Tokens
-   Role-Based Authorization
-   Rate Limiting
-   CORS
-   Global Exception Handling

# Future Enhancements

-   Multi-tenant support
-   Payroll integration
-   Face recognition attendance
-   Mobile application
-   Microservices architecture
-   Kafka notifications

## License

This project is intended for learning and portfolio purposes.

                STARTUP
──────────────────────────────────────────

#  **flow**

Click Run
│
▼
JVM Starts
│
▼
main()
│
▼
SpringApplication.run()
│
▼
Read application.properties
│
▼
Create ApplicationContext
│
▼
Component Scan
│
▼
Create Beans
│
▼
Inject Dependencies
│
▼
Auto Configuration
│
▼
Create Hikari Connection Pool
│
▼
Initialize Hibernate
│
▼
Start Embedded Tomcat
│
▼
Application Ready

──────────────────────────────────────────

              REQUEST FLOW

Browser/Postman
│
▼
HTTP Request
│
▼
Tomcat
│
▼
DispatcherServlet
│
▼
Handler Mapping
│
▼
Controller
│
▼
Service
│
▼
Repository (Spring Data JPA)
│
▼
Hibernate
│
▼
HikariCP (gets a DB connection)
│
▼
MySQL Database
│
▼
ResultSet
│
▼
Hibernate (maps rows → User objects)
│
▼
Repository
│
▼
Service
│
▼
Controller
│
▼
Jackson (Java → JSON)
│
▼
Tomcat
│
▼
HTTP Response
│
▼
Browser/Postman
