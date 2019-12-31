package org.tim.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Project;

import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, String> {
	Optional<Project> findByName(String name);

	Boolean existsByName(String name);
}
