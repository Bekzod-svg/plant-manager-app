# Plant Manager App

## Introduction
The Plant Manager App is a comprehensive system designed to manage plant installations. It leverages microservices architecture to ensure scalable and robust application management, integrating various functionalities such as user management, plant data processing, and real-time monitoring.

## Architecture
The application is structured around several microservices, each responsible for a specific aspect of the system:
- **API Gateway**: Routes incoming requests to the appropriate services.
- **Config Server**: Centralizes configuration management.
- **Discovery Server**: Manages service registration and discovery.
- **Monitoring Service**: Monitors the performance and health of other services.
- **Notification Service**: Handles notifications for users.
- **Plant Service**: Manages data related to plant installations.
- **User Service**: Handles user authentication and data management.

## Frontend
The frontend utilizes Thymeleaf for dynamic HTML rendering, supported by Bootstrap for styling. The key pages include:
- **Home Page**: Displays user-specific greetings and login success messages.
- **Login and Registration Pages**: Handle user authentication and new user registration.
- **Installation Pages**: Allow users to add, edit, and view detailed information about plant installations, including geographical mapping.

## Installation
### Prerequisites
- Java 11 or newer
- Maven for dependency management
- PostgreSQL database

### Setup Guide
1. Clone the repository and navigate into each service directory.
2. Build each service using Maven:
   ```bash
   mvn clean install
