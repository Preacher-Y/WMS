# Warehouse Management System (WMS)

A production-style backend WMS built with **Spring Boot**, modelling the core operations of a real-world warehouse: receiving goods, tracking inventory across locations, moving stock, and maintaining a full audit trail of every movement.

The project demonstrates correct business rules, database transactions, concurrent-modification safety, and a cleanly testable domain design through a layered, DDD-influenced architecture.

**Full project spec and task breakdown:** [Logistics Capstone — Notion](https://quartz-hemisphere-61a.notion.site/Logistics-Capstone-Project-08094a1a2f4c83e9adb6814588a281d7)

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 21 |
| Framework | Spring Boot | 4.1.1 |
| Persistence | Spring Data JPA / Hibernate | via parent |
| Database (production) | MySQL | 8+ |
| Database (tests) | H2 in-memory | via parent |
| Security | Spring Security + JWT (JJWT) | 0.12.6 |
| Mapping | MapStruct | 1.6.3 |
| Boilerplate | Lombok | 1.18.48 |
| API Docs | Custom interactive docs page (`/api/docs`) | — |
| Build | Maven | — |
| Env config | springboot4-dotenv | 5.1.0 |
| Containers | Docker + Docker Compose | — |
| Hosting | Render (Blueprint) | — |

---

## Architecture

The codebase is organized by **feature package** (vertical slice), not by layer. Each feature owns its full stack: controller → service → repository → entity/domain.

```
src/main/java/.../
├── common/
│   ├── docs/               # Serves the public /docs page
│   ├── exceptions/         # Base exception hierarchy (NotFoundException, ConflictException, …)
│   ├── security/           # JWT filter, SecurityConfig, UserDetailsServiceImpl
│   └── utils/              # GlobalExceptionHandler
└── feature/
    ├── auth/               # Login, token refresh, logout
    ├── user/               # Registration, user management
    ├── warehouse/          # Warehouse master data
    ├── location/           # Location master data (row-section-shelf grid)
    ├── cartonHeader/       # Carton type / template
    ├── SSCC/               # Serial Shipping Container Codes (individual carton instances)
    ├── recieve/            # Receive SSCC into a location
    ├── item/               # Inventory item (itemNumber × location)
    ├── moveItem/           # Move stock between locations
    └── stockHistory/       # Immutable audit log of every stock movement
```

### Domain / Entity Separation (DDD)

Every fully refactored feature (warehouse is the reference implementation) separates two concerns:

| Class | Role |
|---|---|
| `domain/<Module>.java` | Pure POJO — holds business state. Used throughout the service layer. No JPA annotations. |
| `entity/<Module>Entity.java` | JPA-annotated persistence class. Never crosses the service boundary. |
| `mapper/<Module>Mapper.java` | MapStruct mapper — bridges DTOs ↔ domain ↔ entity. |
| `repository/<Module>RepositoryPort.java` | Interface the service depends on. Works only with domain objects. |
| `repository/<Module>RepositoryAdapter.java` | `@Component` that implements the port using the JPA repository. |

This means a database schema change (new column, renamed table) touches only the entity and mapper — the service and domain class stay untouched.

### Repository Port/Adapter Pattern

```
Service → RepositoryPort (interface)
                ↑
       RepositoryAdapter (JPA bridge)  ←→  JpaRepository<Entity, Long>
```

Unit tests (`ServiceTests`) inject a `InMemory*Repository` — a `HashMap`-backed implementation of the port — giving fast, zero-Spring, zero-database service tests.

---

## API Docs Page

Open **`http://localhost:{SERVER_PORT}/api/docs`**. No login is needed to view it.

It's a hand-built reference for every endpoint (it replaces Swagger UI, which is disabled):

- **Endpoint reference**: each endpoint has a plain-language description, its parameters and body fields, every response code with an example, and who can call it (**Open**, **Sign-in** or **Admin only**).
- **Live requests**: *Send* calls the running API. Sign in from the sidebar or the `/auth/login` panel and your token is attached to protected calls automatically. *Copy as cURL* gives you the equivalent command.
- **Floor view**: an animated 3D warehouse that shows the request's journey. A forklift (or an office clerk, for account endpoints) passes the security gate, which checks the token and role and turns away 401/403. It then goes to the API server and on to the pallet rack (or the filing cabinet), and returns with the status code. Use *Simulate as Guest / User / Admin* to see how each role is treated without calling the server.

The page is a single static file, `src/main/resources/static/docs.html`. Its endpoint list is written by hand, so **update it when you add or change an endpoint**.

---

## Requirements

- Java 21
- Maven 3.9+
- MySQL 8+ (running on port 3306 by default), **or** Docker

---

## Configuration Profiles

| File | Active when | Database URL | SQL logging |
|---|---|---|---|
| `application.yaml` | Always (shared settings) | none here | n/a |
| `application-dev.yaml` | Default, e.g. running from the IDE | `DB_URL` from `.env`, else `localhost:3306/wms` (no TLS) | On |
| `application-prod.yaml` | `SPRING_PROFILES_ACTIVE=prod` (set by the Docker image) | Built from `DB_HOST` / `DB_PORT` / `DB_NAME`, TLS required | Off |

In either profile, setting `DB_URL` overrides the default.

---

## Local Setup

### 1. Clone the repository

```bash
git clone <repository-url>
cd "Warehouse Management System"
```

### 2. MySQL Setup

Start MySQL and run:

```sql
CREATE DATABASE wms_db;
```

Ensure your MySQL user has `ALL PRIVILEGES` on `wms_db`.

### 3. Configure environment variables

Copy the example file and fill in your values:

```bash
cp .env.example .env
```

Edit `.env`:

```env
DB_URL=jdbc:mysql://localhost:3306/wms_db
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
SERVER_PORT=8080
JWT_SECRET=<base64-encoded 256-bit key>
```

Generate a JWT secret with `openssl rand -base64 32`.

> **Do not commit `.env`** — it is listed in `.gitignore`.

### 4. Run the application

```bash
./mvnw spring-boot:run
```

Or open `WarehouseManagementSystemApplication.java` in IntelliJ and use the pre-configured **Run WMS** run configuration (loads `.env` automatically via the dotenv plugin).

The API is served under the `/api` context path: `http://localhost:{SERVER_PORT}/api`.
Docs page: `http://localhost:{SERVER_PORT}/api/docs`

---

## Running with Docker

`docker-compose.yml` runs MySQL 8.4 with a persistent volume and the API built from the `Dockerfile`, which uses the `prod` profile. It reads `JWT_SECRET` from your `.env`.

```bash
docker compose up --build
```

- Docs page: `http://localhost:8000/api/docs`
- Stop with `docker compose down`. Add `-v` to also delete the database volume.
- The database user and password default to `wms` / `wms`. Override them with `MYSQL_PASSWORD` and `MYSQL_ROOT_PASSWORD` in `.env`.

---

## Deploying to Render

Render doesn't run docker-compose files. It uses the **Blueprint** in `render.yaml` instead, which creates:

| Service | Type | Plan | Notes |
|---|---|---|---|
| `wms-api` | Web service (Docker) | Free | Health check on `/api/docs`; `JWT_SECRET` is generated automatically |
| `wms-mysql` | Private service (`mysql:8.4`) | Starter (paid) | 5 GB persistent disk; passwords generated automatically |

The app reaches MySQL over Render's private network with TLS. The connection details are passed to it automatically.

1. Push the repository to GitHub or GitLab.
2. In the Render dashboard, go to **New → Blueprint**, select the repository, and click **Apply**.
3. When it's live, open `https://<your-service>.onrender.com/api/docs`.
4. Register your account, then promote it to admin from the **Shell** tab of `wms-mysql`:
   ```bash
   mysql -uroot -p"$MYSQL_ROOT_PASSWORD" wms -e "UPDATE users SET role='ADMIN' WHERE email='you@example.com';"
   ```

> Free web services sleep when idle, so the first request after a pause takes 30–60 seconds. Private services and disks need a paid plan; Render has no free MySQL.

---

## Environment Variables

| Variable | Description | Example |
|---|---|---|
| `DB_URL` | Full JDBC URL. Overrides the profile default | `jdbc:mysql://localhost:3306/wms_db` |
| `DB_HOST` / `DB_PORT` / `DB_NAME` | Used to build the URL in the `prod` profile | `db` / `3306` / `wms` |
| `DB_USERNAME` | MySQL user | `root` |
| `DB_PASSWORD` | MySQL password | `secret` |
| `SERVER_PORT` | Port the server binds to | `8080` |
| `JWT_SECRET` | Base64-encoded HMAC key, at least 256 bits | `openssl rand -base64 32` |
| `JWT_ACCESS_EXPIRATION_MS` | Access token lifetime (default 15 min) | `900000` |
| `JWT_REFRESH_EXPIRATION_MS` | Refresh token lifetime (default 7 days) | `604800000` |
| `SPRING_PROFILES_ACTIVE` | `dev` (default) or `prod` | `prod` |

---

## Running Tests

```bash
# Full suite (91 tests: unit + integration)
./mvnw test

# Integration tests only
./mvnw test -Dtest="WarehouseFlowIntegrationTest"

# Single module
./mvnw test -Dtest="WarehouseServiceTests"
```

Tests use an **H2 in-memory database** with `ddl-auto: create-drop` — no MySQL required. The test `application.yaml` supplies all credentials. No `.env` file needed to run tests.

### Test Layers

| Layer | Annotation | Speed | What it tests |
|---|---|---|---|
| Repository | `@DataJpaTest` + H2 | Fast | JPA queries, unique constraints, optimistic locking |
| Service | `@ExtendWith(MockitoExtension)` | Fastest | Business logic, exceptions — uses `InMemory*Repository` |
| Controller | `@WebMvcTest` | Fast | HTTP mapping, status codes, JSON serialization |
| Integration | `@SpringBootTest` + `@Transactional` | Slower | Full request → response → database round-trip |

---

## API Reference

> The interactive version of this reference, with live requests, is at **`/api/docs`**.

All paths below are relative to the `/api` context path (for example `POST /auth/login` is `http://localhost:{SERVER_PORT}/api/auth/login`). The move and history controllers have their own `/api` prefix, so their full URLs are `/api/api/move` and `/api/api/history`.

All endpoints except the four public ones (and the docs page) require a JWT Bearer token:

```
Authorization: Bearer <accessToken>
```

Tokens are obtained from `POST /auth/login`.

### Authentication & Users

#### `POST /users` — Register (public)
```json
// Request
{ "email": "user@example.com", "name": "Alice", "password": "secret123" }

// Response 201
{ "id": 1, "email": "user@example.com", "name": "Alice", "active": true, "role": "USER" }
```

#### `POST /auth/login` — Login (public)
```json
// Request
{ "email": "user@example.com", "password": "secret123" }

// Response 200
{ "accessToken": "eyJ...", "refreshToken": "eyJ..." }
```

#### `POST /auth/refresh` — Refresh access token (public)
Accepts the refresh token in the request body and returns a new access token.

#### `POST /auth/logout` — Logout (public)
Invalidates the current refresh token.

#### `GET /users` — List all users — **ADMIN**
Returns array of user objects.

#### `GET /users/{id}` — Get user — authenticated
#### `PUT /users/{id}` — Update user — **ADMIN**

---

### Warehouse

#### `POST /warehouses` — Create warehouse — **ADMIN**
```json
// Request
{ "warehouseNumber": 1, "name": "Main Warehouse" }

// Response 201
{ "id": 1, "warehouseNumber": "WH-001", "name": "Main Warehouse", "active": true, "version": 0 }
```
`warehouseNumber` is supplied as an integer; stored and returned as `"WH-XXX"`.

#### `PUT /warehouses/{warehouseNumber}` — Update warehouse — **ADMIN**
```json
// Request  (warehouseNumber path param: "WH-001")
{ "name": "New Name", "active": false }
```

---

### Location

#### `POST /warehouses/{warehouseId}/locations` — Create location — **ADMIN**
```json
// Request
{ "row": 1, "section": 2, "shelf": 3 }

// Response 201
{ "id": 5, "row": 1, "section": 2, "shelf": 3, "locationCode": "1-2-3", "warehouseId": 1 }
```
`locationCode` is the derived `"{row}-{section}-{shelf}"` string used in all other endpoints.

---

### Carton Header

A carton header is the **template** that describes a carton type (e.g., product line, SKU batch). Multiple SSCCs (individual physical cartons) belong to the same carton header.

#### `POST /carton-headers` — Create — **ADMIN**
```json
// Request
{ "barcode": "BC-001", "description": "Product A Carton" }

// Response 201
{ "id": 3, "barcode": "BC-001", "description": "Product A Carton", "version": 0 }
```

#### `PUT /carton-headers/{barcode}` — Update description — **ADMIN**

---

### SSCC

An SSCC (Serial Shipping Container Code) is a **unique physical carton instance**. Each SSCC belongs to one carton header (many-to-one) and can be received into a location exactly once.

#### `POST /carton-headers/{barcode}/ssccs` — Create SSCC — **ADMIN**
```json
// Request  (barcode path param: "BC-001")
{ "sscc": "SSCC-ABC-001" }

// Response 201
{ "id": 7, "sscc": "SSCC-ABC-001", "cartonHeader": { "id": 3, "barcode": "BC-001", ... }, "receivedTimestamp": null }
```

---

### Receive

Receiving creates or increments the inventory `Item` at the target location, stamps the SSCC with a `receivedTimestamp`, and records a `RECEIVE` event in StockHistory.

#### `POST /receive` — Receive SSCC — authenticated
```json
// Request
{
  "warehouseNumber": "1",     // raw integer string, converted to "WH-001" internally
  "sscc": "SSCC-ABC-001",
  "locationCode": "1-2-3"
}

// Response 200 — the Item at the location after receiving
{ "id": 12, "itemNumber": "BC-001", "quantity": 1, "location": { ... }, "cartonHeader": { ... } }
```

Errors:
- `404` — warehouse or SSCC or location not found
- `409` — SSCC already received, or warehouse inactive

---

### Move

Moves `quantity` units of an item from one location to another within the same warehouse. Decrements the source item, creates or increments the destination item, and records a `MOVE` event in StockHistory.

#### `POST /api/move` — Move stock — authenticated
```json
// Request
{
  "warehouseNumber": "WH-001",
  "itemNumber": "BC-001",
  "fromLocationCode": "1-2-3",
  "toLocationCode": "4-5-6",
  "quantity": 5
}

// Response 200 (no body)
```

Errors:
- `404` — warehouse, source location, destination location, or source item not found
- `400` — warehouse inactive, source = destination, or insufficient stock

---

### Stock History

#### `GET /api/history` — Query audit log — authenticated
```
GET /api/history?warehouseNumber=1&itemNumber=BC-001
GET /api/history?warehouseNumber=1&itemNumber=BC-001&locationCode=1-2-3
```

```json
// Response 200
[
  {
    "id": 1,
    "action": "RECEIVE",
    "itemNumber": "BC-001",
    "quantityChange": 1,
    "fromLocationCode": null,
    "toLocationCode": "1-2-3",
    "warehouseNumber": "WH-001",
    "sscc": "SSCC-ABC-001",
    "createdTimestamp": "2026-10-10T10:00:00"
  }
]
```

`action` values: `RECEIVE`, `MOVE`, `ADJUST`

---

## Domain Model

```
Warehouse (1) ──────────────────────────── (*) Location
                                                  │
                                                  │ (ManyToOne)
                                                  │
CartonHeader (1) ──── (*) SSCC             (*) Item (itemNumber + location = unique)
     │                                            │
     └────────────── (ManyToOne) ─────────────────┘
                   (Item.cartonHeader)

StockHistory — append-only log, one row per RECEIVE / MOVE / ADJUST event
User — application user with role USER or ADMIN
```

### Key Relationships

| Relationship | Cardinality | Notes |
|---|---|---|
| Warehouse → Location | One-to-many | Locations are scoped to one warehouse |
| CartonHeader → SSCC | One-to-many | Multiple physical cartons share one template |
| Item | `(itemNumber, locationId)` unique | Same SKU at two locations = two Item rows |
| SSCC → CartonHeader | Many-to-one | `itemNumber` is derived from `cartonHeader.barcode` |

---

## Design Decisions

### Why `@Transactional` is required on Receive and Move

Both operations touch **multiple tables** in a single business action:

- **Receive**: saves/updates an `Item`, creates a `StockHistory` row, updates `Sscc.receivedTimestamp`.
- **Move**: updates the source `Item`, saves/creates the destination `Item`, creates a `StockHistory` row.

`@Transactional` wraps all of these into a single database transaction. If any step throws an exception, the entire unit is rolled back — no partial state is committed.

### What atomicity means and what a partial commit would break

**Atomicity** means the transaction is all-or-nothing.

Without `@Transactional` on the receive operation, a crash after the `Item.quantity` increment but before the `StockHistory` insert would leave the database in a broken state: inventory went up but there is no audit record explaining why. A developer looking at StockHistory would see a quantity that has no origin. Partial commits make the database untrustworthy.

### What a lost update is

A **lost update** occurs when two concurrent transactions both read the same row, each compute a new value independently, and both write back — the second write silently overwrites the first.

Example without protection:
```
Thread A reads Item.quantity = 10
Thread B reads Item.quantity = 10
Thread A writes quantity = 10 - 3 = 7
Thread B writes quantity = 10 - 5 = 5   ← Thread A's decrement is lost
```
The correct final value should be 2 (10 − 3 − 5). The database now has 5, which is wrong, and no error was raised.

### How `@Version` optimistic locking detects concurrent modification

Every domain entity that can be concurrently modified carries a `@Version Integer version` field. Hibernate automatically:

1. Includes `WHERE id = ? AND version = ?` in every `UPDATE`.
2. Increments `version` on each successful write.

If Thread A and Thread B both read version = 3, and Thread A commits first (version becomes 4), Thread B's `UPDATE` will match zero rows (version 3 no longer exists). Hibernate detects this and throws `ObjectOptimisticLockingFailureException`, which the `GlobalExceptionHandler` maps to **HTTP 409 Conflict**.

The client is expected to retry the operation. No silent data loss occurs.

### Why database unique constraints are still needed even when service-layer checks exist

The service checks (e.g., `if (ssccRepository.existsBySscc(req.sscc())) throw ...`) run inside a single thread before the insert. Under concurrent load, two requests can both pass the check simultaneously (both read "does not exist") and then both attempt to insert — a classic **check-then-act race condition**.

The database unique constraint is enforced atomically by the database engine at insert time, making it the only truly reliable guard. The service-layer check exists for better error messages (before hitting the DB); the DB constraint is the safety net. Both are needed.

### Why request/response DTOs are used instead of exposing entities

Exposing JPA entities directly via REST creates several problems:

- **Coupling**: API shape is tied to the database schema. A column rename breaks the API.
- **Security**: Internal fields (passwords, version counters, audit timestamps) may be accidentally serialized.
- **Circular references**: Bidirectional JPA relationships cause `StackOverflowError` during serialization.
- **Swagger noise**: JPA annotations pollute the generated API documentation.

DTOs define an explicit contract between the API and its consumers that is independent of persistence details.

### Why StockHistory stores snapshot values instead of live entity references

StockHistory is an **immutable audit log**. Each row captures what happened at a point in time.

If `StockHistory` stored a foreign key to `Location` instead of the raw `locationCode` string, then renaming or deleting a location would retroactively change or corrupt historical records. Storing the `locationCode` value as it existed at the time of the event preserves the audit trail permanently, regardless of future changes to master data.

The same principle applies to `warehouseNumber`, `itemNumber`, and `sscc` — all stored as snapshot strings.

### Why inventory uniqueness is based on `itemNumber + location`

A single product (item number = carton header barcode) can be stored in multiple warehouse locations simultaneously. The `Item` table tracks **quantity per location**, so the composite unique constraint `(item_number, location_id)` is the natural business key: "how many units of item X are currently at location Y?"

This model allows:
- Splitting stock across locations
- Moving partial quantities between locations
- Independent quantity tracking per location

---

## Error Handling

All errors are returned as JSON via `GlobalExceptionHandler`.

| Situation | HTTP Status |
|---|---|
| Resource not found | `404 Not Found` |
| Duplicate (e.g., barcode already exists) | `409 Conflict` |
| Concurrent modification (`@Version` conflict) | `409 Conflict` |
| Business rule violation (inactive warehouse, insufficient stock) | `400 Bad Request` |
| Invalid credentials | `401 Unauthorized` |
| Missing or expired token | `401 Unauthorized` |
| Signed in without the required role | `403 Forbidden` |
| Missing or malformed request body / field validation | `400 Bad Request` |

Error response shapes:
```json
// Business errors (404, 409, 400 rule violations, bad credentials)
{ "error": "Warehouse WH-009 not found" }

// Field validation (400): one entry per invalid field
{ "email": "Email must be valid", "name": "Name must be at least 3 characters" }

// Security (401 / 403), written by SecurityConfig
{ "error": "Unauthorized", "message": "Full authentication is required to access this resource" }
```

---

## Git / PR Workflow

### Branch naming

```
feat/<short-description>          # new feature
fix/<short-description>           # bug fix
refactor/<short-description>      # refactor without behaviour change
test/<short-description>          # tests only
```

### Commit style

```
feat: add receive endpoint with stock history recording
fix: change SSCC–CartonHeader from OneToOne to ManyToOne
test: add receive→move integration test
```

### PR process

1. Branch from `main`.
2. Open a PR with a description explaining **what** and **why**.
3. Ensure all CI checks pass (`./mvnw test`).
4. At least one review approval before merge.
5. Squash-merge into `main`.

---

## Known Assumptions & Limitations

| Area | Assumption / Limitation |
|---|---|
| Warehouse number | Supplied as integer on creation; formatted as `WH-XXX` internally. The receive endpoint accepts the raw integer string (`"1"`); the move endpoint requires the formatted form (`"WH-001"`). This inconsistency is a known improvement area. |
| Item number | Derived directly from `cartonHeader.barcode`. Items do not have an independent creation endpoint. |
| SSCC receive | Each SSCC can be received exactly once. Re-receiving is rejected as a conflict. |
| User roles | New users are always created with `Role.USER`. There is no public API to promote a user to `ADMIN`; this must be done directly via the database or a seeded admin. |
| ADJUST stock action | The `StockAction.ADJUST` enum value exists but no endpoint currently triggers it. |
| Pagination | List endpoints return all records without pagination. |
| Soft delete | Warehouses and carton headers support `active` flags but no entities are hard-deleted. Locations, items, and SSCCs have no deactivation mechanism. |
| Integration tests | Use an in-memory H2 database and a programmatically seeded ADMIN user. MySQL-specific behavior (e.g., strict mode, character sets) is not covered by the test suite. |

---

## Presentation Guide

### Project Summary

This is a full-stack backend REST API for a warehouse management domain. It demonstrates:

- **Domain-Driven Design** — domain objects isolated from JPA entities, business logic in the service layer with meaningful domain methods (`deactivate()`, `rename()`).
- **Repository Port/Adapter** — services depend on interfaces, enabling true unit testing without a database.
- **Transaction safety** — `@Transactional` guarantees atomic multi-table operations; `@Version` prevents lost updates under concurrent load.
- **Layered testing** — unit, slice, and full integration tests covering service logic, HTTP contracts, and end-to-end flows.
- **Role-based security** — JWT authentication with `ADMIN` / `USER` roles enforced at the controller level.

### Demo Flow

```
1. Register a user              POST /users
2. Login                        POST /auth/login          → copy accessToken
3. Create warehouse             POST /warehouses          (ADMIN token)
4. Create two locations         POST /warehouses/{id}/locations  × 2
5. Create carton header         POST /carton-headers
6. Create two SSCCs             POST /carton-headers/{barcode}/ssccs  × 2
7. Receive SSCC 1 → source      POST /receive             → quantity = 1
8. Receive SSCC 2 → source      POST /receive             → quantity = 2
9. Move 1 unit to destination   POST /api/move            → source = 1, dest = 1
10. Query audit log             GET  /api/history         → 2 RECEIVE + 1 MOVE
```

### Challenges and Solutions

| Challenge | Root Cause | Solution |
|---|---|---|
| Integration tests failed to load context | Duplicate `app:` key in `src/test/resources/application.yaml` caused YAML parse error | Merged the two `app:` blocks into one |
| Two SSCCs could not share one CartonHeader | `@OneToOne` on `SsccEntity.cartonHeader` adds a DB unique constraint on `cartonHeaderId` | Changed to `@ManyToOne` — correct semantically (SSCC = instance, CartonHeader = type) |
| Integration test got HTTP 403 on ADMIN endpoints | User registered via `POST /users` is always `Role.USER`; no API path to promote to `ADMIN` | `@BeforeEach` seeds an ADMIN user directly via `UserRepositoryPort` |
| Move did not appear in StockHistory | `MoveItemService` never called `StockHistoryService.record()` | Added the `record(MOVE, …)` call after the destination item is saved |
| `ServiceMoveItemTests` broke after adding `StockHistoryService` dependency | `MoveItemService` constructor gained a new parameter | Added `@Mock StockHistoryService historyService` and passed it to the constructor |
