package com.krd.chatassistant.chats;

import com.krd.chatassistant.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
		"spring.ai.openai.api-key=test-key",
		"spring.autoconfigure.exclude=org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
class ChatControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OpenAiChatService openAiChatService;

	@Test
	void createChat_shouldReturn201WithChatDto() throws Exception {
		mockMvc.perform(post("/chats"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(header().exists("Location"));
	}

	@Test
	void sendMessage_withValidRequest_shouldReturn200() throws Exception {
		// Mock OpenAI response
		when(openAiChatService.chat(any(), anyString()))
				.thenReturn("This is an AI response");

		// First create a chat
		String createResponse = mockMvc.perform(post("/chats"))
				.andReturn()
				.getResponse()
				.getContentAsString();

		// Extract chat ID (simple parsing - in real code use JSON library)
		String chatId = createResponse.split("\"id\":\"")[1].split("\"")[0];

		// Send message
		String requestBody = "{\"prompt\":\"Hello, AI!\"}";

		mockMvc.perform(post("/chats/" + chatId + "/messages")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(chatId))
				.andExpect(jsonPath("$.messages").isArray())
				.andExpect(jsonPath("$.messages[0].role").value("USER"))
				.andExpect(jsonPath("$.messages[1].role").value("BOT"));
	}

	@Test
	void sendMessage_withEmptyPrompt_shouldReturn400() throws Exception {
		UUID chatId = UUID.randomUUID();
		String requestBody = "{\"prompt\":\"\"}";

		mockMvc.perform(post("/chats/" + chatId + "/messages")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").isNotEmpty());
	}

	@Test
	void sendMessage_withInvalidChatId_shouldReturn404() throws Exception {
		UUID invalidId = UUID.randomUUID();
		String requestBody = "{\"prompt\":\"Test message\"}";

		mockMvc.perform(post("/chats/" + invalidId + "/messages")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Chat not found with id: " + invalidId));
	}
}
