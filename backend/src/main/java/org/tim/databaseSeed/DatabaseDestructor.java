package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.repositories.*;

import javax.annotation.PreDestroy;

@Service
@RequiredArgsConstructor
public class DatabaseDestructor {

	private final MessageRepository messageRepository;
	private final MessageVersionRepository messageVersionRepository;
	private final ProjectRepository projectRepository;
	private final TranslationAgencyRepository translationAgencyRepository;
	private final TranslationRepository translationRepository;
	private final TranslationVersionRepository translationVersionRepository;

	@PreDestroy
	public void destroyDatabase() {
		messageRepository.deleteAll();
		messageVersionRepository.deleteAll();
		projectRepository.deleteAll();
		translationAgencyRepository.deleteAll();
		translationRepository.deleteAll();
		translationVersionRepository.deleteAll();
	}
}
