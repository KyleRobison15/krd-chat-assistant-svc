package com.krd.chatassistant.chats;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatService {

	private final ChatRepository chatRepository;
	private final ChatMapper chatMapper;
	private final ChatMessageRepository chatMessageRepository;
	private final OpenAiChatService openAiChatService;

	/**
	 * Creates a new chat session.
	 */
	public ChatDto createChat() {
		var chat = new Chat();
		chatRepository.save(chat);

		return chatMapper.toDto(chat);
	}

	/**
	 * Sends a user message to the chat, gets AI response, and saves both messages.
	 *
	 * @param chatId The ID of the chat session
	 * @param prompt The user's message
	 * @return Updated chat with all messages including the new response
	 */
	public ChatDto sendMessage(UUID chatId, @NotNull String prompt) {
		// Find the chat or throw exception
		var chat = chatRepository.findById(chatId)
				.orElseThrow(() -> new ChatNotFoundException(chatId));

		// Get conversation history (excluding the new message)
		var history = chatMessageRepository.findByChatIdOrderByCreatedAtAsc(chatId);

		// Get AI response using the conversation history
		String aiResponse = openAiChatService.chat(history, prompt);

		// Create and save the user message
		var userMessage = ChatMessage.builder()
				.chat(chat)
				.role(ChatMessageRole.USER)
				.content(prompt)
				.build();
		chatMessageRepository.save(userMessage);

		// Create and save the bot response
		var botMessage = ChatMessage.builder()
				.chat(chat)
				.role(ChatMessageRole.BOT)
				.content(aiResponse)
				.build();
		chatMessageRepository.save(botMessage);

		// Reload the chat with all messages to return
		chat = chatRepository.findById(chatId)
				.orElseThrow(() -> new ChatNotFoundException(chatId));

		return chatMapper.toDto(chat);
	}
}
