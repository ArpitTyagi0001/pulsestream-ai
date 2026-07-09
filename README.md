# PulseStream AI

Real-time event analytics platform built with Kafka, Spring Boot microservices, and AI-generated insights.

## About

PulseStream AI tracks location events as they happen. Events are captured, streamed through Kafka, stored, and served through a secure, cached API. Instead of just showing raw numbers, an AI service reads the live data and writes a short, readable summary of what's going on — so the dashboard tells you something, not just shows you something.

This was built to practice designing a real distributed system: multiple services talking to each other, a shared authentication layer, caching where it actually helps, and AI used for something practical.

## How it works

An event comes in through the Event Service and gets published to Kafka. The Consumer Service picks it up and saves it to PostgreSQL. The Query Service exposes that data through cached endpoints, so repeated dashboard requests stay fast. The React frontend logs in with JWT, pulls live data every few seconds, and can trigger the AI service to generate a summary of current activity. One API Gateway sits in front of everything and checks the token on every request.

## Services

- **Auth Service** – login, registration, JWT tokens
- **Event Service** – accepts events, publishes to Kafka
- **Event Consumer Service** – reads from Kafka, saves to the database
- **Query Service** – serves dashboard data, cached with Redis
- **AI Service** – generates a written summary from live stats using Spring AI and Ollama
- **API Gateway** – routes requests, verifies JWT
- **React Dashboard** – login, live stats, and the AI summary button

## Tech stack

Java, Spring Boot, Spring Cloud Gateway, Kafka, PostgreSQL, Redis, Spring Security (JWT), Spring AI, Ollama, React

## Running locally

Kafka, PostgreSQL, Redis, and Ollama need to be running first.

```bash
cd auth-service && ./mvnw spring-boot:run
cd event-service && ./mvnw spring-boot:run
cd event-consumer-service && ./mvnw spring-boot:run
cd query-service && ./mvnw spring-boot:run
cd spring-ai && ./mvnw spring-boot:run
cd api-gateway && ./mvnw spring-boot:run
cd client && npm install && npm start
```

## Next steps

- Docker Compose for one-command setup
- WebSockets instead of polling
- Tests across services
- Role-based access control
