# CORE Banking System (EBTPS)

## Overview
EBTPS is a desktop-based banking application built entirely in **Core Java**. It serves as a comprehensive academic demonstration of advanced Java programming concepts, software architecture, and database management. The application features two distinct portals: a Customer Dashboard for standard banking operations (transfers, loans, analytics), and a Manager Dashboard for administrative oversight (account management, system reporting).

## Architecture
The system strictly adheres to the **Model-View-Controller (MVC)** architectural pattern:
*   **Model (`com.ebtps.model`)**: Pure POJOs (`User`, `Account`, `Transaction`, `Loan`, `Notification`) encapsulating domain data.
*   **View (`com.ebtps.view`)**: Java Swing components (Panels and Frames) responsible solely for rendering the UI and capturing user input. Data logic is entirely decoupled from this layer.
*   **Controller (`com.ebtps.controller`)**: The orchestrators that receive UI input, invoke Service/DAO methods, and route data back to the Views.
*   **Data Access Object (DAO) (`com.ebtps.dao`)**: Interfaces and implementations abstracting all direct `java.sql` operations.
*   **Service (`com.ebtps.service`)**: The business logic layer, handling atomic operations and background threading.

## Core Java & Academic Concepts Demonstrated

### 1. Object-Oriented Programming (OOP)
*   **Encapsulation**: All models use private fields with standard getters.
*   **Polymorphism & Interfaces**: Data access relies on interfaces (e.g., `LoanDAO`) implemented by concrete classes (`LoanDAOImpl`), allowing for easy mocking or future database swaps.
*   **Custom Exceptions**: Robust error handling via `InsufficientBalanceException`, `AccountFrozenException`, and others, cleanly propagating business rules to the UI layer.

### 2. Concurrency & Multi-Threading
*   **Double-Checked Locking**: The `DatabaseConnection` and `SessionManager` Singletons utilize `volatile` variables and `synchronized` blocks to guarantee thread safety.
*   **Thread Pools (`ExecutorService`)**: The `NotificationService` dispatches database writes to a fixed thread pool, ensuring that heavy I/O operations do not block the Swing Event Dispatch Thread (EDT).
*   **Daemon Threads**: The `AuditLogger` implements `Runnable` and runs silently in the background, polling system metrics every 60 seconds.
*   **Shutdown Hooks**: Registered via `Runtime.getRuntime().addShutdownHook()` to gracefully terminate thread pools when the JVM exits.

### 3. Advanced Database Management (JDBC & PostgreSQL)
*   **Atomic Transactions**: The `TransactionService` disables auto-commit (`conn.setAutoCommit(false)`) to execute multi-step money transfers atomically, utilizing explicit `commit()` and `rollback()` for data integrity.
*   **Pessimistic Locking**: Uses SQL `SELECT ... FOR UPDATE` to lock account rows during a transfer, preventing race conditions if concurrent transfers involve the same account.
*   **Aggregate Queries**: The `ReportDAO` executes complex analytics using `SUM()`, `COUNT()`, and `GROUP BY` functions to generate customer expense reports.

### 4. Java File I/O
*   **System Auditing**: The `AuditLogger` daemon thread utilizes `FileWriter` and `BufferedWriter` to append system health snapshots to a local `audit.log` text file.

## Setup Instructions

### Prerequisites
1.  **Java Development Kit (JDK) 8 or higher**.
2.  The provided **PostgreSQL JDBC Driver** (`postgresql.jar`) included in the project root.

### Database Connection
The application is pre-configured to connect to a cloud-hosted **Supabase PostgreSQL database**. No local database installation or initialization is required. The schema and default users are already populated on the cloud server.

### Running the Application
A batch script is provided to easily run the application on Windows:
```cmd
run.bat
```

Alternatively, you can compile the source files and execute the main entry point manually:
```bash
javac -d bin -sourcepath src src/com/ebtps/main/Application.java
java -cp "bin;postgresql.jar" com.ebtps.main.Application
```
*(Note: Modify the classpath separator `:` or `;` depending on your OS. For Windows, use `;`).*

## Default Test Credentials

**Manager Portal:**
*   Username: `admin`
*   Password: `admin123`

**Customer Portal:**
*   Username: `johndoe`
*   Password: `password123`
