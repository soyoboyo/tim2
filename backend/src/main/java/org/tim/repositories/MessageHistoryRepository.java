package org.tim.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.MessageHistory;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageHistoryRepository extends CrudRepository<MessageHistory, String> {

	List<MessageHistory> findAllByMessageIdOrderByUpdateDateDesc(String originalMessageId, Pageable pageable);

	List<MessageHistory> findAllByMessageIdAndUpdateDateBeforeOrderByUpdateDateDesc(String messageId, Date updateDate, Pageable pageable);

	List<MessageHistory> findAllByMessageIdAndUpdateDateBeforeAndUpdateDateAfterOrderByUpdateDateDesc(String messageId, Date upperBound, Date lowerBound, Pageable pageable);

}
