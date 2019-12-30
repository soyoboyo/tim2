package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.entities.MessageHistory;
import org.tim.repositories.MessageHistoryRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageVersionService {


	//TODO DO WYWALENIA !!!


	private final MessageHistoryRepository messageHistoryRepository;

	public List<MessageHistory> getMessageVersionsByMessageIdAndUpdateDate(String messageId, Date updateDate) {
		return messageHistoryRepository.findAllByMessageIdAndUpdateDateBeforeOrderByUpdateDateDesc(messageId, updateDate);
	}

	public List<MessageHistory> getMessageVersionsByMessageIdAndUpdateDateBetween(String messageId, Date upperBound, Date lowerBound) {
		return messageHistoryRepository.findAllByMessageIdAndUpdateDateBeforeAndUpdateDateAfterOrderByUpdateDateDesc(messageId, upperBound, lowerBound);
	}
}
