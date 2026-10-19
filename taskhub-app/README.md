# TaskHub

Web app with user registration/login and personal tasks stored in MySQL.

**Stack:** Java 21, Spring Boot 3.3, Spring Security (BCrypt), Spring Data JPA, Thymeleaf, MySQL 8, JUnit 5

## Features
- Register and login (passwords hashed with BCrypt)
- Each user sees and edits only their own tasks (create, edit, delete, change status)
- Admin role with a page listing all registered users
- Default admin created on first start: `admin@taskhub.com` / `Admin@123` (change in `application.properties`)

## Run (IntelliJ IDEA)
1. Start MySQL (Workbench connection must work).
2. Open `src/main/resources/application.properties` and set your MySQL username/password.
3. IntelliJ: File -> Open -> select `pom.xml` -> Open as Project. Set Project SDK to JDK 21.
4. Wait for Maven to download dependencies, then run `TaskHubApplication`.
5. Open http://localhost:8080

## Run (terminal)
```bash
mvn spring-boot:run
```

## View the data in MySQL Workbench
Connect to localhost, open schema `taskhub_db`, then run the queries in `database/useful-queries.sql`.

## Tests
```bash
mvn test
```
(tests use an in-memory H2 database, MySQL is not needed for them)
