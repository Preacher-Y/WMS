# Warehouse Management System (WMS)

A backend Warehouse Management System built with **Spring Boot**, modeling the core operations of a real-world warehouse: receiving goods, tracking inventory across locations, moving stock, and maintaining a full audit trail of every movement.

The project focuses on **business rules, transactions, concurrency, and testable design**.

**Tech stack:** Java 21 · Spring Boot 4.1.1 · Spring Data JPA/Hibernate · MySQL · Maven · Springdoc OpenAPI

**Full project spec, task breakdown, and requirements:**
[Logistics Capstone Project — Notion](https://quartz-hemisphere-61a.notion.site/Logistics-Capstone-Project-08094a1a2f4c83e9adb6814588a281d7)

---

## Prerequisites

- Java 21
- Maven
- MySQL (running on port 3306)

---

## Setup

### 1. Clone the repository

```bash
git clone <repository-url>
cd "Warehouse Management System"
```

### 2. Create the database

```sql
CREATE DATABASE wms_db;
```

### 3. Configure environment variables

Copy the example env file and fill in your values:

```bash
cp .env.example .env
```

Edit `.env`:

```env
DB_URL=jdbc:mysql://localhost:<database_port>/<database_name>
DB_USERNAME=your_username
DB_PASSWORD=your_password
SERVER_PORT=your_desired_port
```

### 4. Run the application

```bash
./mvnw spring:boot:run
```

Or run `WarehouseManagementSystemApplication.java` directly from your IDE. In IntelliJ, the run configuration (`Run WMS`) is pre-configured to load the `.env` file automatically.

---

## Project Structure

```
src/main/java/group/australia/warehouse_management_system/
│
├── WarehouseManagementSystemApplication.java
│
├── common/                         # Shared utilities used across features
│   ├── dtos/
│   │   └── ErrorDto.java
│   ├── exceptions/
│   │   └── NotFoundException.java
│   └── utils/
│       ├── GlobalExceptionHandler.java
│       └── PageResponse.java
│
└── feature/                        # Domain features (feature-based packaging)
    ├── warehouse/
    │   ├── controller/
    │   │   └── WarehouseController.java
    │   ├── dto/
    │   │   ├── WarehouseRequestDto.java
    │   │   └── WarehouseResponseDto.java
    │   ├── entity/
    │   │   └── Warehouse.java
    │   ├── repository/
    │   │   └── WarehouseRepository.java
    │   └── service/
    │       └── WarehouseService.java
    │
    ├── location/
    │   ├── dto/
    │   │   ├── LocationRequestDto.java
    │   │   └── LocationResponseDto.java
    │   └── entity/
    │       └── Location.java
    │
    ├── item/
    │   ├── dto/
    │   │   ├── ItemRequestDto.java
    │   │   └── ItemResponseDto.java
    │   └── entity/
    │       └── Item.java
    │
    ├── SSCC/
    │   ├── dto/
    │   │   ├── SsccRequestDto.java
    │   │   └── SsccResponseDto.java
    │   └── entity/
    │       └── SSCC.java
    │
    ├── cartonHeader/
    │   ├── controller/
    │   │   └── CartonHeaderController.java
    │   ├── dto/
    │   │   ├── CartonHeaderRequestDto.java
    │   │   └── CartonHeaderResponseDto.java
    │   ├── entity/
    │   │   └── CartonHeader.java
    │   ├── repository/
    │   │   └── CartonHeaderRepository.java
    │   └── service/
    │       └── CartonHeaderService.java
    │
    └── stockHistory/
        ├── controller/
        │   └── StockHistoryController.java
        ├── dto/
        │   ├── StockHistoryRequestDto.java
        │   └── StockHistoryResponseDto.java
        ├── entity/
        │   └── StockHistory.java
        ├── repository/
        │   └── StockHistoryRepository.java
        └── service/
            └── StockHistoryService.java
```

---

> **NOTE**: When you run the App and meet some issues just know it the issue for its mostly because i created the place holders of the codes so it might fail 