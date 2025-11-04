# UserManagement

# 🧑‍💼 User Management System (Spring Boot)

A complete **User Management System** built using **Spring Boot**, **Spring Data JPA**, **Hibernate**, **Spring Security**, **Spring JWT (api, impl, jackson)**, **Spring OAuth2** for Google Login, and **Spring Boot Actuator** for monitoring and metrics.  
This project implements secure, role-based authentication and authorization using **MySQL** as the backend database.

---

## 🚀 Features

- 🔐 **Authentication & Authorization**

  - Spring Security with **JWT tokens** (Spring JWT `api`, `impl`, and `jackson` modules)
  - Role-based access control (`ADMIN`, `USER`)
  - Secure login and registration endpoints

- 🌐 **Google OAuth2 Login**

  - Login using Google credentials via Spring OAuth2

- 🧱 **Database Integration**

  - Spring Data JPA + Hibernate ORM
  - MySQL relational database backend

- ⚙️ **Monitoring with Spring Actuator**

  - Real-time health, metrics, and log monitoring
  - Custom actuator configuration in `application.properties`

- 📦 **Clean REST API Design**
  - Layered architecture
  - JSON request/response via Jackson

---

## 🧩 Tech Stack

| Technology                            | Purpose                             |
| ------------------------------------- | ----------------------------------- |
| Spring Boot                           | Application Framework               |
| Spring Data JPA                       | ORM Abstraction                     |
| Hibernate                             | ORM Implementation                  |
| MySQL                                 | Database                            |
| Spring Security                       | Authentication & Authorization      |
| Spring JWT (`api`, `impl`, `jackson`) | Token-based Authentication          |
| Spring OAuth2                         | Google Login Integration            |
| Spring Boot Actuator                  | Monitoring and Application Insights |
| Jackson                               | JSON Processing                     |

---

## 🗄️ Database Setup (MySQL)

- (In mysql workbench)

  - CREATE DATABASE user_management;
