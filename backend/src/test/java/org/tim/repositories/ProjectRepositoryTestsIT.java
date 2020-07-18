package org.tim.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.configuration.SpringJpaTestsCustomExtension;
import org.tim.entities.Project;

import java.util.Arrays;
import java.util.Optional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectRepositoryTestsIT extends SpringJpaTestsCustomExtension {

	@Autowired
	private ProjectRepository projectRepository;

	@Test
	@DisplayName("Check if finding projects is working correct.")
	public void testProjectRepositoryFind() {
		//given
		Project project = projectRepository.save(random(Project.class));
		//when
		Project actualProject = projectRepository.findAllById(Arrays.asList(project.getId())).get(0);
		//then
		assertAll(
				() -> assertNotNull(actualProject),
				() -> assertEquals(project, actualProject)

		);
	}

	@Test
	@DisplayName("Check if updating projects is working correct.")
	public void testProjectRepositoryUpdate() {
		//given
		Project project = projectRepository.save(random(Project.class));
		project.setName("TIM 2.0");
		projectRepository.save(project);
		//when
		Optional<Project> actualProject = projectRepository.findById(project.getId());
		//then
		assertAll(
				() -> assertTrue(actualProject.isPresent()),
				() -> assertEquals(project, actualProject.get())
		);
	}

	@Test
	@DisplayName("Check if deleting projects is working correct.")
	public void testProjectRepositoryDelete() {
		//given
		Project project = projectRepository.save(random(Project.class));
		projectRepository.delete(project);
		//when
		Optional<Project> actualProject = projectRepository.findById(project.getId());
		//then
		assertAll(
				() -> assertFalse(actualProject.isPresent())
		);
	}
}
