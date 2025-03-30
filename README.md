# E-Commerce Web Application

## Overview
This is a full-stack e-commerce web application built using Spring Boot and Thymeleaf. The project includes authentication, role-based access control, product management, and JWT-based security.

## Features
- User authentication (JWT-based login and registration)
- Role-based access control (Admin, Seller, Buyer)
- Product management for sellers
- Order placement for buyers
- Thymeleaf-based frontend

## Technologies Used
- **Backend:** Spring Boot, Spring Security, Spring Data JPA
- **Frontend:** Thymeleaf, Bootstrap
- **Database:** MySQL
- **Security:** JWT Authentication
- **Tools:** Maven, Eclipse, Postman

## Installation & Setup
### Prerequisites
- Java 21 (JDK 21)
- MySQL Database
- Maven

### Clone Repository
```sh
 git clone https://github.com/your-repository/ecommerce-app.git
 cd ecommerce-app
```

### Configure Database
Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=youruser
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### Build & Run
```sh
mvn clean install
mvn spring-boot:run
```

### Access the Application
- Register/Login: `http://localhost:8080/register` or `http://localhost:8080/login`
- API Endpoints:
  - `POST /auth/login` - Login and receive a JWT token
  - `GET /admin/**` - Admin features
  - `GET /seller/**` - Seller features
  - `GET /buyer/**` - Buyer features
