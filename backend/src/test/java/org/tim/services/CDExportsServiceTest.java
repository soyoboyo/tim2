package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Project;
import org.tim.repositories.ProjectRepository;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CDExportsServiceTest extends SpringTestsCustomExtension {

	@Autowired
	private CDExportsService cdExportsService;

	@Autowired
	private ProjectRepository projectRepository;

	@BeforeEach
	void setUp() {
		Project project = createEmptyGermanToEnglishProject();
		createTenRandomMessages(project);
		createTranslationsForMessages();
	}

	@Test
	void whenExportingProjectBySourceLocaleThenReturnExistingMessages() {
		//given
		//when
		Map<String, String> exported = cdExportsService.exportAllReadyTranslationsByProjectAndByLocale(4L, Locale.GERMAN.toString());
		//then
		assertEquals(10, exported.size());
	}

	@Test
	void whenExportingProjectByTargetLocaleThenReturnExistingMessages() {
		//given
		//when
		Map<String, String> exported = cdExportsService.exportAllReadyTranslationsByProjectAndByLocale(4L, Locale.ENGLISH.toString());
		//then
		assertEquals(10, exported.size());
	}
}