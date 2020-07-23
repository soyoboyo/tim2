package org.tim.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Project;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	Boolean existsByName(String name);

	Optional<Project> findByName(String name);
}
