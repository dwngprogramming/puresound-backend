# PureSound Backend - Pure Music Essence

![Build Status](https://img.shields.io/github/actions/workflow/status/dwngprogramming/puresound-backend/.github/workflows/production-ci-cd.yml?style=flat-square&logo=github)
![Java](https://img.shields.io/badge/Java-21%2B-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green?style=flat-square&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?style=flat-square&logo=docker)
![MySQL](https://img.shields.io/badge/MySQL-8.0.38-blue?style=flat-square&logo=mysql)

**PureSound Backend** is the core processing system for the PureSound music streaming platform. This project provides robust RESTful APIs
for managing tracks, albums, artists, users and protected audio streaming.

## 🚀 Key Features

* **Music Streaming:** High-performance, low-latency audio streaming API.
* **Storage Management:** Integrated with MinIO for secure and efficient media file storage/retrieval.
* **Security:** Comprehensive user authentication and authorization mechanisms.
* **Containerization:** Fully Dockerized for rapid deployment and scalability.
* **CI/CD:** Automated build pipelines via GitHub Actions.

## 🛠 Tech Stack

* **Language:** Java (JDK 21 or later)
* **Framework:** Spring Boot
* **Build Tool:** Gradle
* **Database:** MySQL 
* **Object Storage:** MinIO
* **Infrastructure:** Docker, Nginx

## 📋 Prerequisites

Before you begin, ensure you have the following installed on your machine:

* [JDK 21+](https://www.oracle.com/java/technologies/downloads/)
* [Docker & Docker Compose](https://www.docker.com/)
* [Gradle](https://gradle.org/) (Optional, wrapper is included)
* [Laragon](https://laragon.org/) (Optional, if you want to manage database by tool)

## ⚙️ Installation & Running

### 1. Clone the repository

```bash
git clone https://github.com/dwngprogramming/puresound-backend.git
cd puresound-backend
```

### 2. Configure Environment Variables (In Dev Environment)

Edit `.env.dev` file in the root directory or update the `src/main/resources/application-dev.yml` file. Below is an example configuration required to run the application (*demo for spring.datasource*):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your-database
    username: your-username
    password: your-password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 3. Run with Gradle (Development)

You can run the application directly using the included Gradle wrapper.

**On Linux/macOS:**
```bash
./gradlew bootRun
```

**On Windows:**
```bash
./gradlew.bat bootRun
```

### 4. Run with Docker (Production/Staging)

This project is fully Dockerized. You can build and run the container using the following commands:

**Build the Docker image:**
```bash
docker build -t puresound-backend .
```

**Run the container:**
```bash
docker run -d -p 8080:8080 --name puresound-api puresound-backend
```

### 5. Deploy & CI/CD (Staging & Production)
This project applies a `Blue-Green Deployment` strategy to ensure zero downtime.
- **Staging:** Pushing changes to the `develop` branch triggers the pipeline via `staging-ci-cd.yml` and automatically deploys to the `Staging` environment.
- **Production:** Pushing changes to the `main` branch triggers the pipeline via `production-ci-cd.yml` and automatically deploys to the `Production` environment.

### 6. Project Structure

A high-level overview of the source code organization:

```plaintext
src/main/java/com/puresound/backend
├── api/          # REST API (API Endpoints)
├── api_docs      # OpenAPI documentation for API Endpoints
├── config/       # Configuration classes
├── dto/          # Data Transfer Objects (Request/Response models)
├── entity/       # Database Models (JPA Entities)
├── exception/    # Global Exception Handling
├── mapper/       # Data Mapping (Using MapStruct)
├── repository/   # Data Access Layer (JPA Repositories)
├── security/     # Security Config & Custom (Based on Spring Security)
├── service/      # Business Logic & Service Implementations
└── util/         # Util Class
```

### 7. Contact
This is a personal project developed by me, an aspiring Backend Developer. If you have any questions or feedback, please contact me via email.
- **Email:** [dwnq.coding@gmail.com](mailto:dwnq.coding@gmail.com) or [dungphamminh.dev@gmail.com](mailto:dungphamminh.dev@gmail.com)
- **My LinkedIn:** [LinkedIn](https://www.linkedin.com/in/dungphamminh/)

### 8. License
This project is licensed under the GNU General Public License v3.0 (GPLv3). See the [LICENSE](LICENSE) file for details.