# LiveSignalMap

Backend for Wi-Fi and mobile signal heatmap in university buildings.

## 📖 About

(Что делает проект, зачем, для кого)

## 🛠 Tech Stack

- Java 21
- Spring Boot 3.5.10
- PostgreSQL 17
- Liquibase
- QueryDSL
- OpenClover (coverage)
- Docker + GitHub Actions

## 🚀 Getting Started

### Prerequisites
- Java 21
- Docker Desktop
- Maven (или использовать `./mvnw`)

### Run with Docker (recommended)
docker-compose up --build

### Run locally
./mvnw spring-boot:run

## 🔌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/measurements/wifi | Submit Wi-Fi measurement |
| POST | /api/measurements/mobile | Submit mobile measurement |
| GET | /api/heatmap/wifi | Wi-Fi heatmap |
| GET | /api/heatmap/mobile | Mobile heatmap |
| GET | /actuator/health | Health check |

### Swagger UI
http://localhost:8080/swagger-ui.html

## 🧪 Testing

./mvnw clean verify

- 21 tests passing
- Coverage: 57.1% (code), 71.4% (classes)
- Testcontainers for isolation
- OpenClover report: `target/site/clover/index.html`

## 🐳 Docker

docker-compose up --build

## 🔧 Configuration

Environment variables:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

## 📊 Coverage

![Clover Report](docs/clover-screenshot.png)

## 📝 Design Decisions

- **Kafka not used**: for the current load (hundreds of measurements per semester),
  monolith + PostgreSQL is sufficient. Kafka would be added if load grows
  or if microservices are introduced.
- **Aggregation on server**: client sends raw measurements, server computes
  averages. This ensures a single source of truth and allows clients
  (Android, iOS, web) to stay thin.
- **Filtering**: measurements with zero or unrealistic values are excluded
  from heatmap. Groups with fewer than 3 measurements are hidden
  to avoid misleading statistics.

## 👥 Team

- Backend: Maxim
- Mobile: Maxim
- UI/UX: (name)
- QA: (name)

## 📄 License

Educational project. Not for commercial use.
