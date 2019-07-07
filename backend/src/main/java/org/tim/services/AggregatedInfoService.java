package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.entities.AggregatedInfoForDeveloper;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AggregatedInfoService {

	private final ProjectRepository projectRepository;
	private final MessageRepository messageRepository;
	private final TranslationRepository translationRepository;

	public AggregatedInfoForDeveloper getAggregatedInfoForDeveloper(Long projectId) {
		AggregatedInfoForDeveloper aifd = new AggregatedInfoForDeveloper();
		aifd.setProjectId(projectId);


		return aifd;
	}
}
