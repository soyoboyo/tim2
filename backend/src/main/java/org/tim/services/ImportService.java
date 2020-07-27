package org.tim.services;

import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tim.DTOs.MessageDTO;
import org.tim.DTOs.input.TranslationCreateDTO;
import org.tim.constants.TranslationStatus;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

	private final int skippedLinesInDevFile = 2;

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

		final CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		CSVParser csvParser = new CSVParser(reader, csvFormat);

		saveMessages(csvParser.getRecords(), project.getId());
	}

	@Transactional
	public void importTranslatorCSVFile(MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new Exception("The file is empty.");
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

		createTranslations(csvParser.getRecords());
	}

	private void createTranslations(List<CSVRecord> records) throws Exception {
		for (int i = 0; i < records.size(); i += MESSAGE_BLOCK) {
			String key = null;
			String locale = null;
			String translation = null;
			String translationStatus = null;
			try {
				key = records.get(i + KEY_ROW).get(KEY_COLUMN);
				locale = records.get(i + LOCALE_ROW).get(LOCALE_COLUMN);
				translation = records.get(i + NEW_TRANSLATION_ROW).get(NEW_TRANSLATION_COLUMN);
				translationStatus = records.get(i + TRANSLATION_STATUS_ROW).get(TRANSLATION_STATUS_COLUMN);
			} catch (Exception exception) {
				throw new Exception("Check if your delimiter is set to \",\" (comma)");
			}

			if (!translationStatus.equals(TranslationStatus.Missing.name())) {
				continue;
			} else if (translation.isBlank()) {
				continue;
			}

			TranslationCreateDTO translationCreateDTO = new TranslationCreateDTO(translation, locale);

			Optional<Message> messageOptional = messageRepository.findByKey(key);

			if (messageOptional.isPresent()) {
				translationService.createTranslation(translationCreateDTO, messageOptional.get().getId());
			} else {
				throw new EntityNotFoundException(key);
			}
		}
	}

	private void saveMessages(List<CSVRecord> records, Long projectId) {
		for (CSVRecord record : records) {
			String key = null;
			String content = null;
			String description = null;
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

	private String getAndSkipLine(BufferedReader reader) throws IOException {
		String projectNameRaw = reader.readLine();
		String projectName = clearDelimiterFromProjectName(projectNameRaw, 1);

		return projectName;
	}

	private String getAndSkipLine(BufferedReader reader, int delimiterLength) throws IOException {
		String projectNameRaw = reader.readLine();
		String projectName = clearDelimiterFromProjectName(projectNameRaw, delimiterLength);

		return projectName;
	}

	private String clearDelimiterFromProjectName(String projectNameRaw, int delimiterLength) {
		return projectNameRaw.substring(0, projectNameRaw.length() - delimiterLength);
	}
}
