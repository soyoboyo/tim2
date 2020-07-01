package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.DTOs.input.ProjectDTO;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Project;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.LocaleWrapperRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.tim.constants.UserMessages.*;

public class ProjectServiceTestIT extends SpringTestsCustomExtension {

	@Autowired
	private ProjectService projectService;
	@Autowired
	private LocaleWrapperRepository localeWrapperRepository;

	private ProjectDTO projectDTO;
	private static Project expectedProject;

	@BeforeEach
	public void setUp() {
		Map<String, String> replaceableLocaleToItsSubstituteString = new HashMap<>();
		replaceableLocaleToItsSubstituteString.put("pl_PL", "en_GB");
		replaceableLocaleToItsSubstituteString.put("en_GB", "en_US");
		replaceableLocaleToItsSubstituteString.put("en_US", "de_DE");
		projectDTO = new ProjectDTO("name", "pl_PL",
				Arrays.asList("pl_PL", "en_GB", "en_US", "de_DE"), replaceableLocaleToItsSubstituteString);
		expectedProject = new Project("name", new Locale("pl", "PL"));
		expectedProject.addTargetLocale(Arrays.asList(new LocaleWrapper(new Locale("pl", "PL")),
				new LocaleWrapper(Locale.UK),
				new LocaleWrapper(Locale.US),
				new LocaleWrapper(Locale.GERMANY)));
		expectedProject.updateSubstituteLocale(new LocaleWrapper(
				new Locale("pl", "PL")), new LocaleWrapper(Locale.UK));
		expectedProject.updateSubstituteLocale(new LocaleWrapper(Locale.UK), new LocaleWrapper(Locale.US));
		expectedProject.updateSubstituteLocale(new LocaleWrapper(Locale.US), new LocaleWrapper(Locale.GERMANY));
	}

	@Test
	void createNewProject_DataCorrect_Success() {
		Project responseProject = projectService.createProject(projectDTO);
		validateResponseProject(responseProject);
	}

	@Test
	void updateProject_DataCorrect_Success() {
		Map<String, String> replaceableLocaleToItsSubstituteString = new HashMap<>();
		replaceableLocaleToItsSubstituteString.put("en_US", "en_GB");
		ProjectDTO oldProjectDTO = new ProjectDTO("oldName", "en_US",
				Arrays.asList("en_GB", "en_US"), replaceableLocaleToItsSubstituteString);
		Long projectId = projectService.createProject(oldProjectDTO).getId();
		projectDTO.setName("newName");
		expectedProject.setName("newName");
		Project responseProject = projectService.updateProject(projectDTO, projectId);

		validateResponseProject(responseProject);
	}

	private void validateResponseProject(Project responseProject) {
		assertTrue(responseProject.getName().equals(expectedProject.getName()));
		expectedProject.getTargetLocales().forEach(targetLocale -> {
			assertTrue(responseProject.getTargetLocales().contains(targetLocale));
		});
		assertEquals(expectedProject.getTargetLocales().size(), responseProject.getTargetLocales().size());
		Map<Locale, Locale> responseReplaceableLocaleToItsSubstituteAsLocale = new HashMap<>();
		responseProject.getReplaceableLocaleToItsSubstitute().forEach(
				(replaceableLocaleWrapper, substituteLocaleWrapper) -> {
					responseReplaceableLocaleToItsSubstituteAsLocale.put(
							replaceableLocaleWrapper.getLocale(), substituteLocaleWrapper.getLocale());
				});
		Map<Locale, Locale> expectedReplaceableLocaleToItsSubstituteAsLocale = new HashMap<>();
		expectedProject.getReplaceableLocaleToItsSubstitute().forEach(
				(replaceableLocaleWrapper, substituteLocaleWrapper) -> {
					expectedReplaceableLocaleToItsSubstituteAsLocale.put(
							replaceableLocaleWrapper.getLocale(), substituteLocaleWrapper.getLocale());
				});
		expectedReplaceableLocaleToItsSubstituteAsLocale.forEach((replaceableLocale, substituteLocale) -> {
			assertTrue(responseReplaceableLocaleToItsSubstituteAsLocale.get(replaceableLocale).equals(substituteLocale));
		});
		assertEquals(expectedProject.getReplaceableLocaleToItsSubstitute().size(),
				responseProject.getReplaceableLocaleToItsSubstitute().size());
	}

	@Test
	void createNewProject_DataCorrectWithExistingDateInDatabase_Success() {
		LocaleWrapper localeWrapperToSave = new LocaleWrapper(Locale.JAPAN);
		LocaleWrapper savedLocaleWrapper = localeWrapperRepository.save(localeWrapperToSave);
		projectDTO = new ProjectDTO("name2", "pl_PL",
				Arrays.asList("pl_PL", "en_GB", "en_US", "de_DE", "ja_JP"), projectDTO.getReplaceableLocaleToItsSubstitute());
		projectDTO.getReplaceableLocaleToItsSubstitute().put("de_DE", "ja_JP");
		expectedProject.addTargetLocale(Arrays.asList(new LocaleWrapper(Locale.JAPAN)));
		expectedProject.getReplaceableLocaleToItsSubstitute().put(
				new LocaleWrapper(Locale.GERMANY), new LocaleWrapper(Locale.JAPAN));
		Project responseProject = projectService.createProject(projectDTO);
		expectedProject.getTargetLocales().forEach(targetLocale -> {
			assertTrue(responseProject.getTargetLocales().contains(targetLocale));
		});
		assertTrue(responseProject.getTargetLocales().contains(savedLocaleWrapper));
		assertEquals(expectedProject.getTargetLocales().size(), responseProject.getTargetLocales().size());
		Map<Locale, Locale> responseReplaceableLocaleToItsSubstituteAsLocale = new HashMap<>();
		responseProject.getReplaceableLocaleToItsSubstitute().forEach(
				(replaceableLocaleWrapper, substituteLocaleWrapper) -> {
					responseReplaceableLocaleToItsSubstituteAsLocale.put(
							replaceableLocaleWrapper.getLocale(), substituteLocaleWrapper.getLocale());
				});
		Map<Locale, Locale> expectedReplaceableLocaleToItsSubstituteAsLocale = new HashMap<>();
		expectedProject.getReplaceableLocaleToItsSubstitute().forEach(
				(replaceableLocaleWrapper, substituteLocaleWrapper) -> {
					expectedReplaceableLocaleToItsSubstituteAsLocale.put(
							replaceableLocaleWrapper.getLocale(), substituteLocaleWrapper.getLocale());
				});
		expectedReplaceableLocaleToItsSubstituteAsLocale.forEach((replaceableLocale, substituteLocale) -> {
			assertTrue(responseReplaceableLocaleToItsSubstituteAsLocale.get(replaceableLocale).equals(substituteLocale));
		});
		assertEquals(expectedProject.getReplaceableLocaleToItsSubstitute().size(),
				responseProject.getReplaceableLocaleToItsSubstitute().size());
	}

	@Test
	void createNewProject_tReplaceableLocaleToItsSubstituteWithIncorrectLocaleFormat_ThrowException() {
		projectDTO.getReplaceableLocaleToItsSubstitute().put("pl_PL", "incorrectFormat");
		Exception exception = assertThrows(ValidationException.class, () ->
				projectService.createProject(projectDTO));
		assertTrue(exception.getMessage().contains("Replaceable locale " +
				"to it's substitute as: pl_PL->incorrectFormat was given in the wrong format."));
	}

	@Test
	void createNewProject_TargetLocalesWithIncorrectLocaleFormat_ThrowException() {
		ProjectDTO newProjectDTO = new ProjectDTO("name", "pl_PL",
				Arrays.asList("incorrectFormat", "en_GB", "en_US", "de_DE"),
				projectDTO.getReplaceableLocaleToItsSubstitute());
		Exception exception = assertThrows(ValidationException.class, () ->
				projectService.createProject(newProjectDTO));
		assertTrue(exception.getMessage().contains("Target locales: " +
				"incorrectFormat was given in the wrong format."));
	}

	@Test
	void createNewProject_SourceLocaleWithIncorrectLocaleFormat_ThrowException() {
		projectDTO.setSourceLocale("incorrectFormat");
		Exception exception = assertThrows(ValidationException.class, () ->
				projectService.createProject(projectDTO));
		assertTrue(exception.getMessage().contains("Source locale: " +
				"incorrectFormat was given in the wrong format."));
	}

	@Test
	void createNewProject_IncorrectReplaceableLocaleToItsSubstitute_ThrowException() {
		String incorrectLocale = "en_GB_PL";
		projectDTO.getReplaceableLocaleToItsSubstitute().put("pl_PL", incorrectLocale);
		Exception exception = assertThrows(ValidationException.class, () ->
				projectService.createProject(projectDTO));
		assertEquals(exception.getMessage(), formatMessage(LCL_NOT_FOUND, incorrectLocale));
	}

	@Test
	void updateProject_IncorrectReplaceableLocaleToItsSubstitute_ThrowException() {
		projectDTO.setName("randomName2");
		Long projectId = projectService.createProject(projectDTO).getId();
		String incorrectLocale = "en_GB_PL";
		projectDTO.getReplaceableLocaleToItsSubstitute().put("pl_PL", incorrectLocale);
		Exception exception = assertThrows(ValidationException.class, () ->
				projectService.updateProject(projectDTO, projectId));
		assertEquals(exception.getMessage(), formatMessage(LCL_NOT_FOUND, incorrectLocale));
	}

	@Test
	void createNewProject_ReplaceableLocaleToItsSubstituteMakeCycle_ThrowException() {
		projectDTO.getReplaceableLocaleToItsSubstitute().put("de_DE", "pl_PL");
		Exception exception = assertThrows(ValidationException.class, () ->
				projectService.createProject(projectDTO));
		assertEquals(exception.getMessage(), LCL_CYCLES);
	}

	@Test
	void updateProject_ReplaceableLocaleToItsSubstituteMakeCycle_ThrowException() {
		projectDTO.setName("randomName");
		projectDTO.getReplaceableLocaleToItsSubstitute().put("de_DE", "pl_PL");
		Long projectId = createEmptyGermanToEnglishAndFrenchProject().getId();
		Exception exception = assertThrows(ValidationException.class, () ->
				projectService.updateProject(projectDTO, projectId));
		assertEquals(exception.getMessage(), LCL_CYCLES);
	}

	@Test
	void getAllProjects_ReturnAllProjects() {
		Project project1 = createEmptyGermanToEnglishProjectWithSubstituteLocales();
		Project project2 = createEmptyGermanToEnglishAndFrenchProject();
		List<Project> projects = projectService.getAllProjects();
		assertEquals(projects.size(), 2);
		for (Project project : projects) {
			if (project.getId() == project1.getId())
				compareProjects(project, project1);
			else if (project.getId() == project2.getId())
				compareProjects(project, project2);
		}
	}


	@Test
	@DisplayName("Check if getAllProjectsForDeveloper() returns proper objects")
	public void getAllProjectsForDeveloper_ReturnAllProjects(){

	}

	private void compareProjects(Project p1, Project p2) {
		assertAll(
				() -> assertEquals(p1.getName(), p2.getName()),
				() -> assertEquals(p1.getSourceLocale(), p2.getSourceLocale()),
				() -> assertEquals(p1.getTargetLocales().size(), p2.getTargetLocales().size()),
				() -> assertEquals(p1.getReplaceableLocaleToItsSubstitute().size(), p2.getReplaceableLocaleToItsSubstitute().size())
		);
		for (LocaleWrapper localeWrapper : p1.getTargetLocales()) {
			assertTrue(p2.getTargetLocales().contains(localeWrapper));
		}

		for (Map.Entry<LocaleWrapper, LocaleWrapper> entry : p1.getReplaceableLocaleToItsSubstitute().entrySet()) {
			assertEquals(p2.getReplaceableLocaleToItsSubstitute().get(entry.getKey()), entry.getValue());
		}
	}
}
