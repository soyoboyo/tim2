package org.tim.repositories;

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
    public void testProjectRepositoryFind() {
        //given
        Project project = projectRepository.save(random(Project.class));
        //when
        Project actualProject = projectRepository.findAllById(Arrays.asList(project.getId())).get(0);
        //then
        assertTrue(actualProject != null);
        assertTrue(project.equals(actualProject));
    }

    @Test
    public void testProjectRepositoryUpdate() {
        //given
        Project project = projectRepository.save(random(Project.class));
        project.setName("TIM 2.0");
        projectRepository.save(project);
        //when
        Optional<Project> actualProject = projectRepository.findById(project.getId());
        //then
        assertTrue(actualProject.isPresent());
        assertEquals(project, actualProject.get());
    }

    @Test
    public void testProjectRepositoryDelete() {
        //given
        Project project = projectRepository.save(random(Project.class));
        projectRepository.delete(project);
        //when
        Optional<Project> actualProject = projectRepository.findById(project.getId());
        //then
        assertFalse(actualProject.isPresent());
    }
}