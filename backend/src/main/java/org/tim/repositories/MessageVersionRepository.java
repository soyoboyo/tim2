package org.tim.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.MessageVersion;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MessageVersionRepository extends ElasticsearchRepository<MessageVersion, String> {
	List<MessageVersion> findAllByMessageIdOrderByUpdateDateDesc(Long originalMessageId);

	List<MessageVersion> findAllByMessageIdAndUpdateDateBeforeOrderByUpdateDateDesc(Long messageId, LocalDateTime updateDate);
	List<MessageVersion> findAllByMessageIdAndUpdateDateBeforeAndUpdateDateAfterOrderByUpdateDateDesc(Long messageId, LocalDateTime upperBound, Date lowerBound);
}
