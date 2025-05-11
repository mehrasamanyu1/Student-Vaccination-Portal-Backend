# School Vaccination Portal – Backend

This is the backend REST API for the School Vaccination Portal. It is built using Java and Spring Boot, with PostgreSQL as the database.

## Features

- User authentication using JWT
- CRUD operations for students, vaccination drives, and records
- Vaccination reporting (drive-wise, class-wise, summary, missed)
- Export reports in CSV, Excel, and PDF
- Dashboard statistics endpoint

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- JWT Authentication
- Apache POI, iTextPDF (for export)
- Maven

## Prerequisites

- Java 17+
- Maven
- PostgreSQL
- Git

## Running Locally

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/school-vaccination-backend.git
   cd school-vaccination-backend
