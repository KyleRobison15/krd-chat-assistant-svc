package com.krd.chatassistant.exception;

import com.krd.chatassistant.chats.ChatNotFoundException;
import com.krd.starter.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ChatExceptionHandlerTest {

	private ChatExceptionHandler exceptionHandler;
	private WebRequest webRequest;

	@BeforeEach
	void setUp() {
		exceptionHandler = new ChatExceptionHandler();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/chats/test-id/messages");
		webRequest = new ServletWebRequest(request);
	}

	@Test
	void handleChatNotFoundException_shouldReturn404() {
		// Given
		UUID chatId = UUID.randomUUID();
		ChatNotFoundException exception = new ChatNotFoundException(chatId);

		// When
		ResponseEntity<ErrorResponse> response =
				exceptionHandler.handleChatNotFoundException(exception, webRequest);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getStatus()).isEqualTo(404);
		assertThat(response.getBody().getMessage()).contains("Chat not found with id: " + chatId);
	}
}
