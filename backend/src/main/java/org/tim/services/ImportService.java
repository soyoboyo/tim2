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
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImportService {

    private final MessageService messageService;
    private final TranslationService translationService;

    private final ProjectRepository projectRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public void importDeveloperCSVMessage(MultipartFile file) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String projectName = getAndSkipLine(reader);
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
    public void importTranslatorCSVFile(MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String projectName = getAndSkipLine(reader, 2);
        String locale = getAndSkipLine(reader, 2);

        final CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        CSVParser csvParser = new CSVParser(reader, csvFormat);

        createTranslations(csvParser.getRecords(), locale);
    }

    private void createTranslations(List<CSVRecord> records, String locale) throws IllegalArgumentException, EntityNotFoundException {
        for (CSVRecord record : records) {
            String key = null;
            String translation = null;
            try {
                key = record.get("key");
                translation = record.get("translation");
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e + ", or check if your delimiter is set to \",\" (comma)");
            }

            Optional<Message> messageOptional = messageRepository.findByKey(key);
            Message message;

            if (messageOptional.isEmpty()) {
                throw new EntityNotFoundException(key);
            }

            message = messageOptional.get();

            TranslationCreateDTO translationCreateDTO = new TranslationCreateDTO(translation, locale);
            translationService.createTranslation(translationCreateDTO, message.getId());
        }
    }

    private void saveMessages(List<CSVRecord> records, Long projectId) throws IllegalArgumentException, NoSuchElementException {
        for (CSVRecord record : records) {
            String key = null;
            String content = null;
            try {
                key = record.get("key");
                content = record.get("content");
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e + ", or check if your delimiter is set to \",\" (comma)");
            }

            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setProjectId(projectId);
            messageDTO.setKey(key);
            messageDTO.setContent(content);

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
