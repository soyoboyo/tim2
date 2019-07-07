package org.tim.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class AggregatedInfoForDeveloper {

	private Long projectId;

	private Map<String, Long> localeCorrectMessages;

	private Long messagesTotal;

}
