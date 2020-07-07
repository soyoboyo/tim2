package org.tim.services;

import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
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
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImportService {

    private final MessageService messageService;
    private final TranslationService translationService;

    private final ProjectRepository projectRepository;
    private final MessageRepository messageRepository;

    public void importDeveloperCSVMessage(MultipartFile file) throws IOException {
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

    public void importTranslatorCSVFile(MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String projectName = getAndSkipLine(reader, 2);
        String locale = getAndSkipLine(reader, 2);

        final CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        CSVParser csvParser = new CSVParser(reader, csvFormat);

        createTranslations(csvParser.getRecords(), locale);
    }

    private void createTranslations(List<CSVRecord> records, String locale) {
        for (CSVRecord record : records) {
            String key = record.get("key");
            String translation = record.get("translation");

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

    private void saveMessages(List<CSVRecord> records, Long projectId) {
        for (CSVRecord record : records) {
            String key = record.get("key");
            String content = record.get("content");

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
