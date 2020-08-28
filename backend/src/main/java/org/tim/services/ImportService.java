package org.tim.services;

import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tim.DTOs.MessageDTO;
import org.tim.DTOs.input.TranslationCreateDTO;
import org.tim.DTOs.input.TranslationUpdateDTO;
import org.tim.constants.TranslationStatus;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.tim.constants.CSVFileConstants.*;

@Service
@AllArgsConstructor
public class ImportService {

	private final MessageService messageService;
	private final TranslationService translationService;
	private final ProjectRepository projectRepository;
	private final MessageRepository messageRepository;
	private final TranslationRepository translationRepository;

	@Transactional
	public void importDeveloperCSVMessage(MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new Exception("The file is empty.");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		String projectName = getAndSkipLine(reader, 2);
		Optional<Project> optionalProject = projectRepository.findByName(projectName);
		Project project;

		if (optionalProject.isPresent()) {
			project = optionalProject.get();
		} else {
			throw new EntityNotFoundException(projectName);
		}
		saveMessages(new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()).getRecords(), project.getId());
	}

	@Transactional
	public void importTranslatorCSVFile(MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new Exception("The file is empty.");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withRecordSeparator("\r\n").withIgnoreEmptyLines(false).withAllowDuplicateHeaderNames();
		CSVParser csvParser = new CSVParser(reader, csvFormat);
		createTranslations(csvParser.getRecords());
	}

	private void createTranslations(List<CSVRecord> records) throws Exception {
		int i = 0;
		while (i < records.size()) {
			String key, lastUpdated;
			try {
				key = records.get(i + KEY_ROW).get(KEY_COLUMN).trim();
				lastUpdated = records.get(i + LAST_UPDATED_ROW).get(LAST_UPDATED_COLUMN).trim();
			} catch (Exception e) {
				throw new Exception("Check if your delimiter is set to \",\" (comma)");
			}

			while (haveMoreLocales(i, records)) {
				String locale, translationStatus, translation;
				try {
					locale = records.get(i + LOCALE_ROW).get(LOCALE_COLUMN).trim();
					translationStatus = records.get(i + TRANSLATION_STATUS_ROW).get(TRANSLATION_STATUS_COLUMN).trim();
					translation = records.get(i + NEW_TRANSLATION_ROW).get(NEW_TRANSLATION_COLUMN).trim();
				} catch (Exception e) {
					throw new Exception("Check if your delimiter is set to \",\" (comma)");
				}

				if (translation.isBlank()) {
					i += NEXT_LOCALE;
					continue;
				}

				saveOrUpdateTranslation(new TranslationCreateDTO(translation, locale), key, translationStatus, lastUpdated);
				i += NEXT_LOCALE;
			}
			i += NEXT_MESSAGE;
		}
	}

	private boolean haveMoreLocales(int line, List<CSVRecord> records) {
		return line + EMPTY_LINE_ROW < records.size() && !isEmptyLine(records.get(line + EMPTY_LINE_ROW));
	}

	private void saveOrUpdateTranslation(TranslationCreateDTO translationCreateDTO, String messageKey, String translationStatus, String lastUpdated) {
		Optional<Message> messageOptional = messageRepository.findByKey(messageKey);

		if (messageOptional.isPresent()) {
			if (messageOptional.get().getUpdateDate().isEqual(LocalDateTime.parse(lastUpdated))) {
				if (translationStatus.equals(TranslationStatus.Missing.name())) {
					saveTranslation(translationCreateDTO, messageOptional.get().getId());
				} else {
					updateTranslation(translationCreateDTO, messageOptional.get().getId());
				}
			}
		} else {
			throw new EntityNotFoundException(messageKey);
		}
	}

	private void updateTranslation(TranslationCreateDTO translationCreateDTO, Long messageId) {
		Translation translation = translationRepository.findTranslationByLocaleAndMessageId(LocaleUtils.toLocale(translationCreateDTO.getLocale()), messageId);
		TranslationUpdateDTO updateDTO = new TranslationUpdateDTO(translationCreateDTO.getContent());
		translationService.updateTranslation(updateDTO, translation.getId(), messageId);
	}

	private void saveTranslation(TranslationCreateDTO translationCreateDTO, Long messageId) {
		translationService.createTranslation(translationCreateDTO, messageId);
	}

	private boolean isEmptyLine(CSVRecord record) {
		for (int i = 0; i < record.size(); i++) {
			if (StringUtils.isNotBlank(record.get(i))) {
				return false;
			}
		}
		return true;
	}

	private void saveMessages(List<CSVRecord> records, Long projectId) {
		for (CSVRecord record : records) {
			String key, content, description;
			try {
				key = record.get("key");
				content = record.get("content");
				description = record.get("description");
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(e + ", or check if your delimiter is set to \",\" (comma)");
			}

			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setProjectId(projectId);
			messageDTO.setKey(key);
			messageDTO.setContent(content);
			messageDTO.setDescription(description);

			messageService.createMessage(messageDTO);
		}
	}

	private String getAndSkipLine(BufferedReader reader, int delimiterLength) throws IOException {
		String projectNameRaw = reader.readLine();
		return clearDelimiterFromProjectName(projectNameRaw, delimiterLength);
	}

	private String clearDelimiterFromProjectName(String projectNameRaw, int delimiterLength) {
		return projectNameRaw.substring(0, projectNameRaw.length() - delimiterLength);
	}
}
