package com.krd.chatassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Chat Assistant Microservice
 *
 * Provides LLM chat functionality with persistent conversation history.
 * Uses OpenAI's GPT models via Spring AI for chat completions.
 *
 * Features:
 * - Anonymous chat sessions (no authentication required)
 * - Persistent conversation history with MySQL
 * - RESTful API for chat operations
 * - Standardized error responses via exception-handling-starter
 */
@SpringBootApplication
public class ChatAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatAssistantApplication.class, args);
	}

}
