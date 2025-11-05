# UserManagement

# 🧑‍💼 User Management System (Spring Boot)

A complete **User Management System** built using **Spring Boot**, **Spring Data JPA**, **Hibernate**, **Spring Security**, **Spring JWT (api, impl, jackson)**, **Spring OAuth2** for Google Login, and **Spring Boot Actuator** for monitoring and metrics.  
This project implements secure, role-based authentication and authorization using **MySQL** as the backend database.

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

# 🧰 How to Run the Project

## 1️⃣ Database Setup (MySQL)

- (In mysql workbench)
  - `CREATE DATABASE user_management;`

---

## 2️⃣ Configure application.properties

- open application.properties
- Set:
  - Your MySQL username and password
  - Your Google OAuth2 Client ID and Client Secret
  - Your own JWT secret key

---

## 3️⃣ Run the Project

- Option A: Using Maven

  - `mvn clean install`
  - `mvn spring-boot:run`

- Option B: Using an IDE
  - Open the project in IntelliJ IDEA, Eclipse, or Spring Tool Suite
  - Locate the main class:
    - `src/main/java/com/yourpackage/UserManagementApplication.java`
  - Right-click → Run as → Spring Boot App
