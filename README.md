# teachme-auth-service

This repository contains the TeachMe Auth Service API, a microservice for authentication people using JSON Web Tokens (JWT) in the TeachMe platform.

## Development Setup

### Prerequisites

Ensure you have the following installed:

- **Java 17 or obove**
- **A database in MongoDB**

### Starting the Development Environment

1. Clone the repository
2. Configure the connection:
    Add the MongoDB URI to 'application.yml' under the 'MONGODB_URI' property.
3. Configure the connection:
    Add a random string of characters to 'security.properties' under the 'SECRET_KEY' property. **Important:** This key should be kept confidential and not committed to version control. 

### Running the Service
```bash
mvnw spring-boot:run 
```
