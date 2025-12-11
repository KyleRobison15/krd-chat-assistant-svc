package com.krd.chatassistant.chats;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that wraps Spring AI's ChatClient for OpenAI integration.
 * Abstracts away direct HTTP calls and provides a clean interface for chat operations.
 */
@Service
@RequiredArgsConstructor
public class OpenAiChatService {

	private final ChatClient.Builder chatClientBuilder;

	/**
	 * Sends a message to OpenAI with conversation history and returns the response.
	 *
	 * @param conversationHistory List of previous messages in the conversation
	 * @param userPrompt The new user message to send
	 * @return The AI-generated response
	 */
	public String chat(List<ChatMessage> conversationHistory, String userPrompt) {
		// Convert conversation history to Spring AI Message format
		List<Message> messages = conversationHistory.stream()
				.map(this::toSpringAiMessage)
				.collect(Collectors.toList());

		// Add the new user message
		messages.add(new UserMessage(userPrompt));

		// Call OpenAI and get response
		ChatClient chatClient = chatClientBuilder.build();
		String response = chatClient.prompt()
				.messages(messages)
				.call()
				.content();

		return response;
	}

	/**
	 * Converts our ChatMessage entity to Spring AI Message format.
	 */
	private Message toSpringAiMessage(ChatMessage chatMessage) {
		return switch (chatMessage.getRole()) {
			case USER -> new UserMessage(chatMessage.getContent());
			case BOT -> new AssistantMessage(chatMessage.getContent());
		};
	}
}
