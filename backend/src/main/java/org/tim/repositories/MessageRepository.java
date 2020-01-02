package org.tim.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Message;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {

	List<Message> findAllByProjectId(String projectId, Pageable pageable);

	default List<Message> findActiveMessagesByProject(String projectId) {
		return findAllByProjectId(projectId, PageRequest.of(0, (int) count()));
	}

}
