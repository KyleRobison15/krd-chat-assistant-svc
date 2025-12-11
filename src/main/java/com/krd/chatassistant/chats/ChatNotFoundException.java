package com.krd.chatassistant.chats;

import java.util.UUID;

public class ChatNotFoundException extends RuntimeException {
	public ChatNotFoundException() {
		super("Chat not found");
	}

	public ChatNotFoundException(UUID chatId) {
		super("Chat not found with id: " + chatId);
	}
}
