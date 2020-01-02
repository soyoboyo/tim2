package org.tim.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Message;
import org.tim.utils.Pages;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {

	List<Message> findAllByProjectIdAndIsArchived(String projectId, boolean isArchived, Pageable pageable);

	default List<Message> findActiveMessagesByProject(String projectId) {
		return findAllByProjectIdAndIsArchived(projectId, false, Pages.all());
	}

}
