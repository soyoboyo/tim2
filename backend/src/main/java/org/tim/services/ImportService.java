package org.tim.services;

import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tim.DTOs.MessageDTO;
import org.tim.entities.Project;
import org.tim.exceptions.EntityNotFoundException;
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
    private final ProjectRepository projectRepository;

    public void importDeveloperCSVMessage(MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String projectName = getProjectNameAndSkipLine(reader);
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

    private String getProjectNameAndSkipLine(BufferedReader reader) throws IOException {
        String projectNameRaw = reader.readLine();
        String projectName = clearDelimiterFromProjectName(projectNameRaw);

        return projectName;
    }

    private String clearDelimiterFromProjectName(String projectNameRaw) {
        return projectNameRaw.substring(0, projectNameRaw.length() - 1);
    }
}
