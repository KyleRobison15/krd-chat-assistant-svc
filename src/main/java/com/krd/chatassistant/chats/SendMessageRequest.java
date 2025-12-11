package com.krd.chatassistant.chats;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendMessageRequest {
	@NotBlank(message = "Prompt cannot be empty")
	private String prompt;
}
