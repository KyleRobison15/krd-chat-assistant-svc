# Chat Assistant Microservice

A standalone Spring Boot microservice for chatting with OpenAI's GPT models. Provides anonymous chat sessions with persistent conversation history.

## Features

- Anonymous chat sessions (no authentication required)
- Persistent conversation history with MySQL
- OpenAI GPT integration via Spring AI
- RESTful API with Swagger documentation
- Standardized error responses
- Flyway database migrations

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring AI 1.0.3 (OpenAI)
- MySQL 8.3.0
- Flyway
- Gradle
- MapStruct
- Lombok

## Getting Started

### Prerequisites

- Java 21
- MySQL 8.0+
- OpenAI API key

### Setup

1. Clone the repository
2. Create a MySQL database named `chat_assistant_db`
3. Copy `.env` template and configure:
   ```
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   OPENAI_API_KEY=your_openai_api_key
   OPENAI_MODEL=gpt-3.5-turbo
   SPRING_PROFILES_ACTIVE=dev
   ```

4. Run database migrations:
   ```bash
   ./gradlew flywayMigrate
   ```

5. Start the application:
   ```bash
   ./gradlew bootRun
   ```

6. Access Swagger UI at: http://localhost:8080/swagger-ui.html

### API Endpoints

- `POST /chats` - Create a new chat session
- `POST /chats/{id}/messages` - Send a message to a chat

### Testing

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.krd.chatassistant.chats.ChatServiceTest"
```

## Project Structure

```
src/
├── main/
│   ├── java/com/krd/chatassistant/
│   │   ├── ChatAssistantApplication.java
│   │   ├── chats/           # Chat domain (entities, services, controllers)
│   │   └── exception/       # Exception handling
│   └── resources/
│       ├── application*.yaml
│       └── db/migration/    # Flyway migrations
└── test/
    └── java/com/krd/chatassistant/
        ├── chats/           # Chat tests
        └── exception/       # Exception handler tests
```

## Development

See [CLAUDE.md](CLAUDE.md) for detailed development guidelines and architecture documentation.

## License

Proprietary - KRD
