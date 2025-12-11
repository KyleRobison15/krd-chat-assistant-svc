# CLAUDE.md

This file provides guidance to Claude Code when working with the Chat Assistant microservice.

## Project Overview

A standalone Spring Boot microservice for chatting with LLMs (OpenAI GPT models). Provides anonymous chat sessions with persistent conversation history.

**Base Package:** `com.krd.chatassistant`

## Build & Development Commands

### Running the Application
```bash
./gradlew bootRun
```

### Running Tests
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.krd.chatassistant.chats.ChatServiceTest"
```

### Database Migrations
```bash
# Apply pending migrations
./gradlew flywayMigrate

# Show migration status
./gradlew flywayInfo

# Validate applied migrations
./gradlew flywayValidate
```

### Building
```bash
# Build the project
./gradlew build

# Build without tests
./gradlew build -x test
```

## Architecture

### Microservice Design

This is a **standalone microservice** focused solely on chat functionality:
- No authentication or user management
- Anonymous chat sessions
- Simple, focused API surface
- Stateless design (except for database persistence)

### Dependencies

**KRD Starters:**
- `exception-handling-starter:1.0.0` - Standardized error responses

**Key Libraries:**
- Spring Boot 3.4.5 (Web, Data JPA, Validation)
- Spring AI 1.0.3 (OpenAI integration)
- MySQL 8.3.0 with Flyway
- MapStruct 1.5.5 (DTO mapping)
- SpringDoc OpenAPI (API documentation)

### Layer Structure

Standard Spring Boot layered architecture:
- **Entity**: JPA entities (`Chat`, `ChatMessage`)
- **Repository**: Spring Data JPA repositories
- **Service**: Business logic (`ChatService`, `OpenAiChatService`)
- **Controller**: REST endpoints (`ChatController`)
- **DTO**: Data transfer objects with MapStruct mappers
- **Exception**: Domain-specific exception handling

### Entity Relationships

- **Chat**: Represents a conversation session with auto-generated UUID
- **ChatMessage**: Individual messages within a chat, with role (USER/BOT) and content

## Database

**Database Name:** `chat_assistant_db`

### Migration Files
- `V1__create_chat_tables.sql` - Initial chat and chat_messages tables
- `V2__change_timestamp_to_datetime.sql` - Convert TIMESTAMP to DATETIME
- `V3__remove_datetime_defaults.sql` - Remove defaults, let Hibernate manage

### Creating New Migrations

1. Create file: `src/main/resources/db/migration/V{N}__{description}.sql`
2. Use sequential version numbers (V4, V5, etc.)
3. Use descriptive snake_case names after the double underscore
4. Run `./gradlew flywayMigrate` to apply

## Configuration

### Environment Variables

Required variables in `.env` file:
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `OPENAI_API_KEY` - OpenAI API key
- `OPENAI_MODEL` - OpenAI model (default: gpt-3.5-turbo)
- `SPRING_PROFILES_ACTIVE` - Active profile (dev, prod)

### Profile-Specific Configuration

- `application.yaml` - Shared configuration (OpenAI, CORS, Swagger)
- `application-dev.yaml` - Development settings (local MySQL, debug logging)
- `application-prod.yaml` - Production settings
- `application-test.yaml` - Test settings (H2 in-memory database)

## Error Handling

All API errors follow a **standardized error response structure** (`ErrorResponse`) from the exception-handling-starter:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Chat not found with id: abc123",
  "path": "/chats/abc123/messages"
}
```

### Architecture

- **Global Handler** (from exception-handling-starter): Handles common exceptions (validation, malformed JSON, etc.)
- **ChatExceptionHandler**: Handles domain-specific exceptions (ChatNotFoundException)

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`
API docs JSON: `http://localhost:8080/v3/api-docs`

## Current Features

- POST /chats - Create a new chat session
- POST /chats/{id}/messages - Send a message and get AI response
- Persistent conversation history
- Anonymous access (no authentication)

## Possible Enhancements

- Add GET /chats/{id} to retrieve chat history
- Add DELETE /chats/{id} to delete a chat
- Add streaming responses for real-time AI output
- Add rate limiting for OpenAI API calls
- Add user authentication to associate chats with users
- Add system prompts/instructions configuration
- Add support for multiple LLM providers
