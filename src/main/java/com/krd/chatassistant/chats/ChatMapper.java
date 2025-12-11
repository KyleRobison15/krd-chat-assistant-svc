package com.krd.chatassistant.chats;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
	ChatDto toDto(Chat chat);
	ChatMessageDto toDto(ChatMessage chatMessage);
}
