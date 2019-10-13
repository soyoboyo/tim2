package org.tim.DTOs.output;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class AggregatedInfoForDeveloper {

	private Long projectId;

	private List<AggregatedLocale> aggregatedLocales;

	private Integer messagesTotal;

}
