package org.tim.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Project;

@Repository
public interface ProjectRepository extends ElasticsearchRepository<Project, String> {
    Boolean existsByName(String name);
}
