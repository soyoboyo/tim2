package org.tim.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.MessageVersion;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MessageVersionRepository extends ElasticsearchRepository<MessageVersion, String> {
	List<MessageVersion> findAllByMessageIdOrderByUpdateDateDesc(String originalMessageId);

	List<MessageVersion> findAllByMessageIdAndUpdateDateBeforeOrderByUpdateDateDesc(String messageId, Date updateDate);
	List<MessageVersion> findAllByMessageIdAndUpdateDateBeforeAndUpdateDateAfterOrderByUpdateDateDesc(String messageId, Date upperBound, Date lowerBound);
}
