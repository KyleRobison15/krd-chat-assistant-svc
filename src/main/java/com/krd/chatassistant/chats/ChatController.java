package com.krd.chatassistant.chats;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/chats")
@Tag(name = "Chats", description = "Chat conversation management")
public class ChatController {

	private final ChatService chatService;

	@PostMapping
	@Operation(summary = "Create a new chat session")
	public ResponseEntity<ChatDto> createChat(UriComponentsBuilder uriComponentsBuilder) {

		var chatDto = chatService.createChat();

		var uri = uriComponentsBuilder.path("/chats/{id}").buildAndExpand(chatDto.getId()).toUri();

		return ResponseEntity.created(uri).body(chatDto);
	}

	@PostMapping("/{chatId}/messages")
	@Operation(summary = "Send a message to the chat")
	public ResponseEntity<ChatDto> sendMessage(
			@Parameter(description = "The ID of the Chat")
			@PathVariable UUID chatId,
			@Valid @RequestBody SendMessageRequest request) {

		var chatDto = chatService.sendMessage(chatId, request.getPrompt());

		return ResponseEntity.ok(chatDto);
	}
}
