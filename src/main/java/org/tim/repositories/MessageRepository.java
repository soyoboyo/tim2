package org.tim.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findMessagesByProjectIdAndIsRemovedFalse(Long projectId);

    Page<Message> findMessagesByProjectIdAndIsRemovedFalse(Long projectId, Pageable page);
}
