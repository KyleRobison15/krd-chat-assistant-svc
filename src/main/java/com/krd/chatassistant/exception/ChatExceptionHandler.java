package com.krd.chatassistant.exception;

import com.krd.chatassistant.chats.ChatNotFoundException;
import com.krd.starter.exception.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Domain-specific exception handler for the Chat Assistant microservice.
 * Handles chat-specific exceptions.
 *
 * Common exceptions (validation, malformed JSON, etc.) are automatically
 * handled by GlobalExceptionHandler from exception-handling-starter.
 *
 * @see com.krd.starter.exception.GlobalExceptionHandler
 * @see ErrorResponse
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ChatExceptionHandler {

	/**
	 * Handles chat not found errors.
	 * Returns 404 Not Found.
	 */
	@ExceptionHandler(ChatNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleChatNotFoundException(
			ChatNotFoundException ex,
			WebRequest request) {

		ErrorResponse errorResponse = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error(HttpStatus.NOT_FOUND.getReasonPhrase())
				.message(ex.getMessage())
				.path(getRequestPath(request))
				.build();

		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(errorResponse);
	}

	/**
	 * Extracts the request path from WebRequest for inclusion in error responses.
	 */
	private String getRequestPath(WebRequest request) {
		return request.getDescription(false).replace("uri=", "");
	}
}
