package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.repositories.*;

import javax.annotation.PreDestroy;

@Service
@RequiredArgsConstructor
public class DatabaseDestructor {

	private final MessageRepository messageRepository;
	private final MessageHistoryRepository messageHistoryRepository;
	private final ProjectRepository projectRepository;
	private final TranslationAgencyRepository translationAgencyRepository;
	private final TranslationRepository translationRepository;
	private final TranslationHistoryRepository translationHistoryRepository;

	@PreDestroy
	public void destroyDatabase() {
		messageRepository.deleteAll();
		messageHistoryRepository.deleteAll();
		projectRepository.deleteAll();
		translationAgencyRepository.deleteAll();
		translationRepository.deleteAll();
		translationHistoryRepository.deleteAll();
	}
}
