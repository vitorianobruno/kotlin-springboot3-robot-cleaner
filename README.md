# Robot Cleaner - VW Digital:Hub Technical Test

**Author:** Bruno Vitoriano

![](https://img.shields.io/badge/docker-257bd6?style=for-the-badge&logo=docker&logoColor=white)
![](https://img.shields.io/badge/SpringBoot-3-green)
![](https://img.shields.io/badge/-Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![](https://img.shields.io/badge/Prometheus-white?logo=prometheus)
![](https://img.shields.io/badge/-Grafana-000?&logo=Grafana)

---

## 🧠 Overview

This project implements the **Robot Cleaner simulation problem** using **Spring Boot 3**, **Kotlin**, and a **Hexagonal Architecture + Domain-Driven Design**.  
The program reads plain-text input describing a grid and a set of robot instructions, executes their movements, and outputs their final positions — exactly as described in the challenge.

---

## ⚙️ Technologies Used

- **Spring Boot 3** — for fast bootstrapping and dependency injection.
- **Kotlin** — chosen for its expressiveness, null safety, and strong domain modeling capabilities.
- **Maven** — for dependency management and build automation.
- **JUnit 5 + Spring Test** — for unit and integration testing.
- **Docker** — to containerize and run the application easily.
- **GitHub Actions (CI)** — automates building, testing, and Docker image creation.

---

## 🧩 Architectural Decisions

The project follows a **Hexagonal (Ports and Adapters) Architecture**, which clearly separates the core domain logic from infrastructure and delivery mechanisms.

- **Domain layer** → Contains the business entities and logic (`Robot`, `Position`, `Direction`).
- **Application layer** → Contains use cases and orchestration (`SimulateService`, `SimulateUseCase`).
- **Adapters (In)** → REST controllers that expose the simulation functionality via HTTP (`SimulateController`).
- **Adapters (Out)** → Not required for this task, but the architecture is ready to support external integrations.

This architecture improves **testability**, **separation of concerns**, and **future maintainability** — for instance, switching the input format (e.g., JSON instead of plain text) would require only changes in the adapter layer.

---

## 🧱 Rich Domain Model

A **rich domain model** was intentionally used.  
Most of the logic resides within the domain entities themselves, not in service classes:

- `Direction` knows how to rotate and move (`left()`, `right()`, `moveForward()`).
- `Robot` encapsulates command execution (`execute(command, bounds)`), enforcing movement rules and respecting grid boundaries.

This design ensures that:
- Business rules are **enforced consistently** (robots never move out of bounds).
- The model is **self-contained** and **cohesive** — it expresses real-world behavior directly.
- The code is **easier to maintain and test** — since logic sits close to the data it manipulates.
- Adding new behaviors (e.g., “clean cell” or “report status”) would extend the domain model without changing other layers.

In contrast, an **anemic domain model** (where logic lives in services instead of entities) would lead to fragmented logic and weaker encapsulation.  
A rich model avoids this by embedding business rules directly in the entities.

---

## 🧩 Assumptions Made

- Robots that attempt to move outside the defined grid **ignore the move** and remain in place.
- The input format strictly follows the specification (plain text, line-based).
- Commands other than `L`, `R`, or `M` are ignored.
- The workspace starts at `(0, 0)` and extends to the provided upper-right coordinate.

---

## 🧪 Testing

- **Unit tests** verify the simulation logic and domain behaviors (`SimulateServiceTest`).
- **Integration tests** verify the REST endpoint end-to-end (`SimulateControllerTest`).

---

Input:
```
5 5
1 2 N
LMLMLMLMM
3 3 E
MMRMMRMRRM
```
Output:
```
1 3 N
5 1 E
```

---

## 🚀 Running the Application

### Build and Run Locally
```bash
mvn clean package
java -jar target/robot-cleaner-0.1.0-SNAPSHOT.jar
```
OR use the provided **python script** to run the application in a container:
```bash
 python build_and_run.py
```

### 🧪 Test the Flow Locally

```bash
curl -X POST http://localhost:8080/api/simulate   -H "Content-Type: text/plain" -d "5 5
1 2 N
LMLMLMLMM
3 3 E
MMRMMRMRRM"
```

---

### 🌐 Build and run in Containers + Monitoring

```bash
docker-compose -f docker-compose.yml up -d
```

### 🧪 Test the containers Flow

```bash
curl -X POST http://localhost:8081/api/simulate   -H "Content-Type: text/plain" -d "5 5
1 2 N
LMLMLMLMM
3 3 E
MMRMMRMRRM"
```

### Monitoring Endpoints:

- Prometheus: http://localhost:9090

- Alerts: http://localhost:9090/alerts

- Grafana: http://localhost:3000 (admin/admin)

Verify that alerts are loaded:
In Prometheus (http://localhost:9090/alerts), you should see your four alerting rules listed.

Alerts are evaluated every evaluation_interval (15s in this case) and triggered when the conditions are met for the time specified in the for statement.

### Quickly Dashboard for Grafana:
- Add a new dashboard.
```bash
{
  "dashboard": {
    "title": "Robot Cleaner Metrics",
    "panels": [
      {
        "title": "Requests per Second",
        "type": "stat",
        "targets": [
          {
            "expr": "sum(rate(http_server_requests_seconds_count{uri=\"/api/simulate\"}[1m]))",
            "legendFormat": "req/s"
          }
        ]
      },
      {
        "title": "Average Response Time",
        "type": "stat",
        "targets": [
          {
            "expr": "rate(http_server_requests_seconds_sum{uri=\"/api/simulate\"}[5m]) / rate(http_server_requests_seconds_count{uri=\"/api/simulate\"}[5m])",
            "legendFormat": "seconds"
          }
        ]
      }
    ]
  }
}
```

In Grafana:
- Go to Connections → Data sources → Prometheus.
- Check the URL field.
- If both are in Docker Compose, it should be: http://prometheus:9090 and click "SAVE"

Now you shold be able to query:
- simulator_requests_total
- simulator_errors_total
- simulator_duration_seconds_count

---

This project includes a complete GitHub CI/CD pipeline that automatically builds, tests, and packages the application on every push and pull request.

## 🚀 Pipeline Features

- **✅ Unit Testing** - Automated Maven test execution
- **🐳 Docker Build** - Optimized container build with Buildx and cache
- **📦 Container Registry** - Automatic publishing to GitHub Container Registry