package com.krd.chatassistant.chats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

	@Mock
	private ChatRepository chatRepository;

	@Mock
	private ChatMapper chatMapper;

	@Mock
	private ChatMessageRepository chatMessageRepository;

	@Mock
	private OpenAiChatService openAiChatService;

	@InjectMocks
	private ChatService chatService;

	private Chat testChat;
	private ChatDto testChatDto;
	private UUID testChatId;

	@BeforeEach
	void setUp() {
		testChatId = UUID.randomUUID();
		testChat = new Chat();
		testChat.setId(testChatId);
		testChat.setMessages(new ArrayList<>());

		testChatDto = new ChatDto();
		testChatDto.setId(testChatId);
	}

	@Test
	void createChat_shouldReturnChatDto() {
		// Given
		when(chatRepository.save(any(Chat.class))).thenReturn(testChat);
		when(chatMapper.toDto(testChat)).thenReturn(testChatDto);

		// When
		ChatDto result = chatService.createChat();

		// Then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(testChatId);
		verify(chatRepository).save(any(Chat.class));
		verify(chatMapper).toDto(testChat);
	}

	@Test
	void sendMessage_withValidChatId_shouldReturnUpdatedChat() {
		// Given
		String userPrompt = "Hello, AI!";
		String aiResponse = "Hello, human!";

		when(chatRepository.findById(testChatId)).thenReturn(Optional.of(testChat));
		when(chatMessageRepository.findByChatIdOrderByCreatedAtAsc(testChatId))
				.thenReturn(new ArrayList<>());
		when(openAiChatService.chat(anyList(), eq(userPrompt))).thenReturn(aiResponse);
		when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(i -> i.getArgument(0));
		when(chatMapper.toDto(testChat)).thenReturn(testChatDto);

		// When
		ChatDto result = chatService.sendMessage(testChatId, userPrompt);

		// Then
		assertThat(result).isNotNull();
		verify(chatRepository, times(2)).findById(testChatId);
		verify(chatMessageRepository, times(2)).save(any(ChatMessage.class));
		verify(openAiChatService).chat(anyList(), eq(userPrompt));
	}

	@Test
	void sendMessage_withInvalidChatId_shouldThrowException() {
		// Given
		UUID invalidId = UUID.randomUUID();
		when(chatRepository.findById(invalidId)).thenReturn(Optional.empty());

		// When/Then
		assertThatThrownBy(() -> chatService.sendMessage(invalidId, "Test"))
				.isInstanceOf(ChatNotFoundException.class)
				.hasMessageContaining("Chat not found with id: " + invalidId);
	}
}
