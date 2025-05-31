# Worklog API Documentation

This document provides detailed information about the Worklog API, including its architecture, controllers, endpoints, and technical specifications.

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technical Stack](#technical-stack)
3. [Project Structure](#project-structure)
4. [API Controllers](#api-controllers)
   - [Employee Controller](#employee-controller)
   - [Grade Controller](#grade-controller)
   - [Worklog Controller](#worklog-controller)
   - [Worklog Type Controller](#worklog-type-controller)
5. [Data Models](#data-models)
6. [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
7. [Configuration](#configuration)
8. [Running the Application](#running-the-application)

## Project Overview

The Worklog API is a Spring Boot application designed to manage employee worklogs, grades, and worklog types. It provides a RESTful interface for creating, reading, updating, and deleting these entities.

## Technical Stack

- **Java 21**: The application is built using Java 21.
- **Spring Boot**: Framework for creating stand-alone, production-grade Spring-based applications.
- **Spring Data JPA**: Simplifies data access using the Java Persistence API.
- **Spring Validation**: Provides validation capabilities for request data.
- **PostgreSQL**: Database for storing application data.
- **Lombok**: Reduces boilerplate code through annotations.
- **OpenAPI/Swagger**: API documentation and testing.
- **Maven**: Build automation tool.

## Project Structure

The project follows a standard Spring Boot application structure:

```
worklog/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── kron/
│   │   │           └── homework/
│   │   │               └── worklog/
│   │   │                   ├── config/         # Configuration classes
│   │   │                   ├── controller/     # REST controllers
│   │   │                   ├── dto/            # Data Transfer Objects
│   │   │                   │   ├── employee/
│   │   │                   │   ├── grade/
│   │   │                   │   ├── worklog/
│   │   │                   │   └── worklogtype/
│   │   │                   ├── exception/      # Custom exceptions
│   │   │                   ├── mapper/         # Object mappers
│   │   │                   ├── model/          # Entity classes
│   │   │                   ├── repository/     # Data repositories
│   │   │                   ├── service/        # Business logic
│   │   │                   └── WorklogApplication.java  # Main class
│   │   └── resources/
│   │       ├── application.properties  # Application configuration
│   │       └── static/                 # Static resources
│   └── test/                           # Test classes
├── mvnw                                # Maven wrapper script (Unix)
├── mvnw.cmd                            # Maven wrapper script (Windows)
├── pom.xml                             # Maven project configuration
└── README.md                           # This file
```

## API Controllers

### Employee Controller

Base URL: `/api/employees`

#### Endpoints

1. **Get All Employees**
   - **Method:** GET
   - **URL:** `/api/employees`
   - **Query Parameters:**
     - `page` (optional, default: 0): Page number
     - `size` (optional, default: 10): Number of items per page
     - `sortBy` (optional, default: "id"): Field to sort by
     - `sortDirection` (optional, default: "asc"): Sort direction ("asc" or "desc")
   - **Response:** Page of EmployeeResponseDto

2. **Get Employee by ID**
   - **Method:** GET
   - **URL:** `/api/employees/{id}`
   - **Path Variable:** `id` (Long)
   - **Response:** EmployeeResponseDto

3. **Create Employee**
   - **Method:** POST
   - **URL:** `/api/employees`
   - **Request Body:** CreateEmployeeDto (JSON)
   - **Response:** EmployeeResponseDto

4. **Update Employee**
   - **Method:** PUT
   - **URL:** `/api/employees/{id}`
   - **Path Variable:** `id` (Long)
   - **Request Body:** UpdateEmployeeDto (JSON)
   - **Response:** EmployeeResponseDto

5. **Delete Employee**
   - **Method:** DELETE
   - **URL:** `/api/employees/{id}`
   - **Path Variable:** `id` (Long)
   - **Response:** No content (204)

### Grade Controller

Base URL: `/api/grades`

#### Endpoints

1. **Get All Grades**
   - **Method:** GET
   - **URL:** `/api/grades`
   - **Query Parameters:**
     - `page` (optional, default: 0): Page number
     - `size` (optional, default: 10): Number of items per page
     - `sortBy` (optional, default: "id"): Field to sort by
     - `sortDirection` (optional, default: "asc"): Sort direction ("asc" or "desc")
   - **Response:** Page of GradeResponseDto

2. **Get Grade by ID**
   - **Method:** GET
   - **URL:** `/api/grades/{id}`
   - **Path Variable:** `id` (Long)
   - **Response:** GradeResponseDto

3. **Create Grade**
   - **Method:** POST
   - **URL:** `/api/grades`
   - **Request Body:** CreateGradeDto (JSON)
   - **Response:** GradeResponseDto

4. **Update Grade**
   - **Method:** PUT
   - **URL:** `/api/grades/{id}`
   - **Path Variable:** `id` (Long)
   - **Request Body:** UpdateGradeDto (JSON)
   - **Response:** GradeResponseDto

5. **Delete Grade**
   - **Method:** DELETE
   - **URL:** `/api/grades/{id}`
   - **Path Variable:** `id` (Long)
   - **Response:** No content (204)

### Worklog Controller

Base URL: `/api/worklogs`

#### Endpoints

1. **Get All Worklogs**
   - **Method:** GET
   - **URL:** `/api/worklogs`
   - **Query Parameters:**
     - `page` (optional, default: 0): Page number
     - `size` (optional, default: 10): Number of items per page
     - `sortBy` (optional, default: "id"): Field to sort by
     - `sortDirection` (optional, default: "asc"): Sort direction ("asc" or "desc")
   - **Response:** Page of WorklogResponseDto

2. **Get Worklog by ID**
   - **Method:** GET
   - **URL:** `/api/worklogs/{id}`
   - **Path Variable:** `id` (Long)
   - **Response:** WorklogResponseDto

3. **Create Worklog**
   - **Method:** POST
   - **URL:** `/api/worklogs`
   - **Request Body:** CreateWorklogDto (JSON)
   - **Response:** WorklogResponseDto

4. **Update Worklog**
   - **Method:** PUT
   - **URL:** `/api/worklogs/{id}`
   - **Path Variable:** `id` (Long)
   - **Request Body:** UpdateWorklogDto (JSON)
   - **Response:** WorklogResponseDto

5. **Delete Worklog**
   - **Method:** DELETE
   - **URL:** `/api/worklogs/{id}`
   - **Path Variable:** `id` (Long)
   - **Response:** No content (204)

6. **Get Worklogs by Employee**
   - **Method:** GET
   - **URL:** `/api/worklogs/employee/{employeeId}`
   - **Path Variable:** `employeeId` (Long)
   - **Query Parameters:**
     - `page` (optional, default: 0): Page number
     - `size` (optional, default: 10): Number of items per page
     - `sortBy` (optional, default: "id"): Field to sort by
     - `sortDirection` (optional, default: "asc"): Sort direction ("asc" or "desc")
   - **Response:** Page of WorklogResponseDto

### Worklog Type Controller

Base URL: `/api/worklog-types`

#### Endpoints

1. **Get All Worklog Types**
   - **Method:** GET
   - **URL:** `/api/worklog-types`
   - **Query Parameters:**
     - `page` (optional, default: 0): Page number
     - `size` (optional, default: 10): Number of items per page
     - `sortBy` (optional, default: "id"): Field to sort by
     - `sortDirection` (optional, default: "asc"): Sort direction ("asc" or "desc")
   - **Response:** Page of WorklogTypeResponseDto

2. **Get Worklog Type by ID**
   - **Method:** GET
   - **URL:** `/api/worklog-types/{id}`
   - **Path Variable:** `id` (Long)
   - **Response:** WorklogTypeResponseDto

3. **Create Worklog Type**
   - **Method:** POST
   - **URL:** `/api/worklog-types`
   - **Request Body:** CreateWorklogTypeDto (JSON)
   - **Response:** WorklogTypeResponseDto

4. **Update Worklog Type**
   - **Method:** PUT
   - **URL:** `/api/worklog-types/{id}`
   - **Path Variable:** `id` (Long)
   - **Request Body:** UpdateWorklogTypeDto (JSON)
   - **Response:** WorklogTypeResponseDto

5. **Delete Worklog Type**
   - **Method:** DELETE
   - **URL:** `/api/worklog-types/{id}`
   - **Path Variable:** `id` (Long)
   - **Response:** No content (204)

## Data Models

The application uses the following entity models:

1. **Employee**: Represents an employee in the organization.
2. **Grade**: Represents an employee grade or level.
3. **Worklog**: Represents a record of work done by an employee.
4. **WorklogType**: Represents a type or category of work.

## Data Transfer Objects (DTOs)

### Employee DTOs
- **CreateEmployeeDto:** Contains fields for creating a new employee (firstName, lastName, gradeId, teamLeadId, directorId, startDate, endDate)
- **UpdateEmployeeDto:** Contains fields for updating an existing employee (id, firstName, lastName, gradeId, teamLeadId, directorId, startDate, endDate)
- **EmployeeResponseDto:** Contains fields returned in employee responses (id, firstName, lastName, gradeId, gradeName, teamLeadId, teamLeadName, directorId, directorName, startDate, endDate)

### Grade DTOs
- **CreateGradeDto:** Contains fields for creating a new grade (name)
- **UpdateGradeDto:** Contains fields for updating an existing grade (name)
- **GradeResponseDto:** Contains fields returned in grade responses (id, name)

### Worklog DTOs
- **CreateWorklogDto:** Contains fields for creating a new worklog (employeeId, monthDate, worklogTypeId, effort)
- **UpdateWorklogDto:** Contains fields for updating an existing worklog (id, employeeId, monthDate, worklogTypeId, effort)
- **WorklogResponseDto:** Contains fields returned in worklog responses (id, employeeId, employeeName, monthDate, worklogTypeId, worklogTypeName, effort)

### Worklog Type DTOs
- **CreateWorklogTypeDto:** Contains fields for creating a new worklog type (name)
- **UpdateWorklogTypeDto:** Contains fields for updating an existing worklog type (name)
- **WorklogTypeResponseDto:** Contains fields returned in worklog type responses (id, name)

Note: All DTOs use appropriate data types (e.g., Long for IDs, String for names, LocalDate for dates, YearMonth for month dates, Integer for effort).

## Configuration

### OpenAPI Configuration

The application uses OpenAPI for API documentation. The configuration is defined in the `OpenApiConfig` class:

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("Worklog API")
                    .version("1.0.0")
                    .description("API for managing worklogs"));
}
```

### Database Configuration

The application uses PostgreSQL as its database. Connection details are configured in the `application.properties` file.

### Maven Configuration

The project uses Maven for dependency management and build automation. Key dependencies include:

- Spring Boot Starter Data JPA
- Spring Boot Starter Validation
- Spring Boot Starter Web
- PostgreSQL Driver
- Lombok

## Running the Application

### Prerequisites

- Java 21 or higher
- PostgreSQL database
- Maven (or use the included Maven wrapper)

### Steps

1. Clone the repository
2. Configure the database connection in `application.properties`
3. Build the application:
   ```
   ./mvnw clean package
   ```
4. Run the application:
   ```
   ./mvnw spring-boot:run
   ```
5. Access the API at `http://localhost:8080/api/`
6. Access the Swagger UI documentation at `http://localhost:8080/swagger-ui.html`