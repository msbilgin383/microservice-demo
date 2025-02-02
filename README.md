# 🚀 Spring Boot Microservices Project

A simple microservices demo with Spring Boot, featuring service discovery, API gateway, and basic CRUD operations.

## 🌟 Services

- 🔍 **Discovery Service** (Eureka) - http://localhost:8761
- 🌐 **Gateway Service** - http://localhost:8080
- 📚 **Book Service** - http://localhost:8082/swagger-ui.html
- 👥 **Customer Service** - http://localhost:8081/swagger-ui.html

## 💻 Tech Stack

- ⚡ Spring Boot & Spring Cloud
- 🗄️ MongoDB
- 📝 Swagger/OpenAPI
- 🔧 Maven

## ⚙️ Prerequisites

- ☕ Java 17+
- 🗄️ MongoDB
- 🔧 Maven

## 🚀 Running the Application

Start services in order:

```bash
# 1. Discovery Service
cd discovery-service
mvn spring-boot:run

# 2. Gateway Service
cd gateway-service
mvn spring-boot:run

# 3. Book Service
cd book-service
mvn spring-boot:run

# 4. Customer Service
cd customer-service
mvn spring-boot:run
```

## 📖 API Documentation

Access Swagger UI:
- 📚 Book Service: http://localhost:8082/swagger-ui.html
- 👥 Customer Service: http://localhost:8081/swagger-ui.html
