package com.krd.chatassistant.chats;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ChatDto {
	private UUID id;
	private List<ChatMessageDto> messages;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
