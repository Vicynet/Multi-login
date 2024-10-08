### Spring Boot Multi-Login Authentication App

**Description:**
This Spring Boot application enables users to authenticate through multiple identifiers such as **username**, **email**, or **phone number**, providing flexibility without compromising security. Built with **Spring Security**, it also includes data persistence through **Spring Data JDBC** for efficient database management and a **Global Exception Handling** mechanism to ensure that errors are consistently managed and reported.

**Features:**
- 🌟 **Multi-Login Authentication**: Supports user login using **username**, **email**, or **phone number**, along with a password for secure authentication.
- 🔒 **Spring Security Integration**: Implements Spring Security to protect your application with customizable authentication and authorization.
- 🧰 **Spring Data JDBC**: Uses **Spring Data JDBC** for efficient database management and simpler CRUD operations, ideal for projects without ORM. DONE ✅
- 🛠 **AOP and Logging**: Leverages **Aspect-Oriented Programming (AOP)** for modular cross-cutting concerns like logging and monitoring. DONE ✅
- ❌ **Global Exception Handling**: Customizes error responses across the entire application using **Spring’s @ControllerAdvice** for consistent and informative error handling. DONE ✅
- 🧑‍🏫 **Spring Validation**: Utilizes **Spring Validation annotations** (`@NotNull`, `@Email`, etc.) to ensure valid user input.
- 📚 **RESTful API**: Provides REST-compliant endpoints for managing user login, registration, and error responses.
- 🔧 **Maven-Based**: All dependencies and project management are handled through Maven for straightforward configuration. DONE ✅
- 📚 **Docker Support**: Easily deploy and run the application in a containerized environment using Docker, simplifying setup and scaling.

**Technologies Used:**
- **Spring Boot**
- **Spring Security**
- **Spring Data JDBC** (for database interactions, support for multiple datasource)
- **PostgreSQL**
- **Maven**
- **Aspect** (for AOP)
- **SLF4J** (for logging)
- **Global Exception Handling** with `@ControllerAdvice`

