package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.DTOs.MessageDTO;
import org.tim.DTOs.input.ProjectDTO;
import org.tim.DTOs.input.TranslationCreateDTO;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Project;
import org.tim.exceptions.ValidationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CIExportsServiceTestIT extends SpringTestsCustomExtension {

	@Autowired
	private ProjectService projectService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private TranslationService translationService;
	@Autowired
	private CIExportsService ciExportsService;

	private ProjectDTO projectDTO;
	private List<MessageDTO> messageDTOList;
	private List<TranslationCreateDTO> translationCreateDTOList;
	private static String expectedResult;

	@BeforeEach
	public void setUp() {
		expectedResult = "#Messages for locale: pl_PL\n" +
				"#" + java.time.LocalDate.now() + "\n" +
				"welcome1=Artykuły spożywcze, które pokochasz, idealnie dostarczone.\n" +
				"welcome2=Niska cena gwarantowana.\n";

		Map<String, String> replaceableLocaleToItsSubstituteString = new HashMap<>();
		replaceableLocaleToItsSubstituteString.put("ar_LY", "pl_PL");
		projectDTO = new ProjectDTO("project", "en_GB",
				Arrays.asList("pl_PL", "en_GB", "ar_LY"), replaceableLocaleToItsSubstituteString);
		messageDTOList = new ArrayList<>(2);

		messageDTOList.add(new MessageDTO("welcome1", "Groceries you’ll love, perfectly delivered.", 1L));
		messageDTOList.add(new MessageDTO("welcome2", "Low Price Promise.", 1L));

		translationCreateDTOList = new ArrayList<>(2);
		translationCreateDTOList.add(new TranslationCreateDTO("Artykuły spożywcze, które pokochasz, idealnie dostarczone.", "pl_PL"));
		translationCreateDTOList.add(new TranslationCreateDTO("Niska cena gwarantowana.", "pl_PL"));
	}

	@Test
	public void exportAllReadyTranslationsByProjectAndByLocale_throwException() {
		// given
		String locale = "ag_XX7d9ww";
		Project project = projectService.createProject(projectDTO);
		for (int i = 0; i < messageDTOList.size(); i++) {
			messageDTOList.get(i).setProjectId(project.getId());
			Long messageId = messageService.createMessage(messageDTOList.get(i)).getId();
			translationService.createTranslation(translationCreateDTOList.get(i), messageId);
		}
		// then
		assertThrows(ValidationException.class, () -> {
			ciExportsService.exportAllReadyTranslationsByProjectAndByLocale(project.getId(), locale); // when
		}, "Locale: " + locale + " doesn't exist.");
	}

	@Test
	public void exportAllReadyTranslationsByProjectAndByLocale_correctOutput() {
		// given
		String locale = "pl_PL";
		Project project = projectService.createProject(projectDTO);
		for (int i = 0; i < messageDTOList.size(); i++) {
			messageDTOList.get(i).setProjectId(project.getId());
			Long messageId = messageService.createMessage(messageDTOList.get(i)).getId();
			translationService.createTranslation(translationCreateDTOList.get(i), messageId);
		}
		// when
		String result = ciExportsService.exportAllReadyTranslationsByProjectAndByLocale(project.getId(), locale);
		// then
		assertAll(
				() -> assertEquals(expectedResult, result)
		);;
	}

	@Test
	public void exportAllReadyTranslationsByProjectAndByLocale_useReplaceableLocale() {
		// given
		String locale = "ar_LY";
		String replaceableLocale = "pl_PL";
		Project project = projectService.createProject(projectDTO);
		for (int i = 0; i < messageDTOList.size(); i++) {
			messageDTOList.get(i).setProjectId(project.getId());
			Long messageId = messageService.createMessage(messageDTOList.get(i)).getId();
			translationService.createTranslation(translationCreateDTOList.get(i), messageId);
		}
		// when
		String result = ciExportsService.exportAllReadyTranslationsByProjectAndByLocale(project.getId(), locale);
		// then
		expectedResult = expectedResult.replace(replaceableLocale, locale);
		assertAll(
				() -> assertEquals(expectedResult, result)
		);;
	}
}
