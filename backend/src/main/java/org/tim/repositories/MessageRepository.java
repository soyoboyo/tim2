package org.tim.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Message;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {

	List<Message> findAllByProjectId(String projectId);

	default List<Message> findActiveMessagesByProject(String projectId) {
		return findAllByProjectId(projectId);
	}

}
