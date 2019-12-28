package org.tim.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Project;

import java.util.Optional;

@Repository
public interface ProjectRepository extends ElasticsearchRepository<Project, String> {
	Optional<Project> findByName(String name);

	Boolean existsByName(String name);
}
