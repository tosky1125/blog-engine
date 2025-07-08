# Blog Engine

A modern blog platform built with Kotlin and Spring Boot, featuring a robust RESTful API, JWT authentication, and advanced content management capabilities.

## 🚀 Features

- **User Management**: Registration, authentication with JWT tokens, and role-based access control
- **Content Management**: Create, read, update, and delete blog posts with rich text support
- **Commenting System**: Nested comments with reply functionality
- **Tagging System**: Organize posts with tags and slug generation
- **Engagement Features**: Like posts and track view counts
- **Caching**: Redis integration for improved performance
- **Search**: Elasticsearch integration for full-text search
- **Monitoring**: Prometheus metrics and health checks

## 🛠️ Tech Stack

- **Language**: Kotlin
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **Caching**: Redis
- **Search**: Elasticsearch
- **Security**: Spring Security + JWT
- **ORM**: JPA/Hibernate
- **Build Tool**: Gradle
- **Testing**: JUnit 5, MockK, TestContainers

## 📋 Prerequisites

- JDK 21+
- PostgreSQL 14+
- Redis 7+
- Elasticsearch 8+ (optional)
- Gradle 8+

## 🔧 Installation

1. Clone the repository
```bash
git clone https://github.com/tosky1125/blog-engine.git
cd blog-engine
```

2. Configure the database
```bash
# Create PostgreSQL database
createdb blog_db
```

3. Update application configuration
```yaml
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/blog_db
    username: your_username
    password: your_password
```

4. Run the application
```bash
./gradlew bootRun
```

## 🏗️ Project Structure

```
src/main/kotlin/com/example/blogenginekotlin/
├── config/          # Configuration classes
├── controller/      # REST API endpoints
├── service/         # Business logic
├── repository/      # Data access layer
├── entity/          # JPA entities
├── dto/             # Data transfer objects
├── security/        # Security configurations
└── exception/       # Custom exceptions
```

## 📚 API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## 🔒 Security

The application uses JWT tokens for authentication. To access protected endpoints:

1. Register a new user via `/api/auth/register`
2. Login via `/api/auth/login` to receive a JWT token
3. Include the token in the `Authorization` header: `Bearer <token>`

## 🧪 Testing

Run the test suite:
```bash
./gradlew test
```

Run with test containers (requires Docker):
```bash
./gradlew test -Dspring.profiles.active=test
```

## 📊 Monitoring

- Health check: `http://localhost:8080/actuator/health`
- Prometheus metrics: `http://localhost:8080/actuator/prometheus`

## 🚦 Roadmap

- [ ] WebSocket support for real-time notifications
- [ ] Image upload and management
- [ ] Email notifications
- [ ] Social media integration
- [ ] Advanced analytics dashboard
- [ ] Multi-language support

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
