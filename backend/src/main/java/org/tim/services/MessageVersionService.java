package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.entities.MessageVersion;
import org.tim.repositories.MessageVersionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageVersionService {

	private final MessageVersionRepository messageVersionRepository;

	public List<MessageVersion> getMessageVersionsByOriginalId(Long originalId) {
		return messageVersionRepository.findAllByMessageIdOrderByUpdateDateDesc(originalId);
	}

	public List<MessageVersion> getMessageVersionsByMessageIdAndUpdateDate(Long messageId, LocalDateTime updateDate) {
		return messageVersionRepository.findAllByMessageIdAndUpdateDateBeforeOrderByUpdateDateDesc(messageId, updateDate);
	}

	public List<MessageVersion> getMessageVersionsByMessageIdAndUpdateDateBetween(Long messageId, LocalDateTime upperBound, LocalDateTime lowerBound) {
		return messageVersionRepository.findAllByMessageIdAndUpdateDateBeforeAndUpdateDateAfterOrderByUpdateDateDesc(messageId, upperBound, lowerBound);
	}
}
