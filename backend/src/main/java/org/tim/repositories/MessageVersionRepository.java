package org.tim.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tim.entities.MessageVersion;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageVersionRepository extends JpaRepository<MessageVersion, Long> {
	List<MessageVersion> findAllByMessageIdOrderByUpdateDateDesc(Long originalMessageId);

	List<MessageVersion> findAllByMessageId(Long messageId);

	List<MessageVersion> findAllByMessageIdAndUpdateDateBeforeOrderByUpdateDateDesc(Long messageId, LocalDateTime updateDate);

	List<MessageVersion> findAllByMessageIdAndUpdateDateBeforeAndUpdateDateAfterOrderByUpdateDateDesc(Long messageId, LocalDateTime upperBound, LocalDateTime lowerBound);
}
