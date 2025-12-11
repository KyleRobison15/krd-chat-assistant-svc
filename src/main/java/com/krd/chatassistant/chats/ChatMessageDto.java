package com.krd.chatassistant.chats;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
	private Long id;
	private ChatMessageRole role;
	private String content;
	private LocalDateTime createdAt;
}
