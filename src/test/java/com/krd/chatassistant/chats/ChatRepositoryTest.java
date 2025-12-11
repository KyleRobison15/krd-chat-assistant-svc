package com.krd.chatassistant.chats;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ChatRepositoryTest {

	@Autowired
	private ChatRepository chatRepository;

	@Test
	void save_shouldPersistChat() {
		// Given
		Chat chat = new Chat();

		// When
		Chat saved = chatRepository.save(chat);

		// Then
		assertThat(saved.getId()).isNotNull();
	}

	@Test
	void findById_shouldReturnChat() {
		// Given
		Chat chat = new Chat();
		Chat saved = chatRepository.save(chat);

		// When
		var found = chatRepository.findById(saved.getId());

		// Then
		assertThat(found).isPresent();
		assertThat(found.get().getId()).isEqualTo(saved.getId());
	}
}
