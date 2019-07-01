package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.tim.DTOs.output.MessageWithWarningsDTO;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Project;
import org.tim.repositories.ProjectRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class XlsConversionService {

    private final MessageTranslationService messageTranslationService;
    private final ProjectRepository projectRepository;
    private static final  String[] columns = {"Key", "Content", "Outdated Translation Content",
            "Substitute Locale", "Substitute Content"};

    public InputStream getXlsForMessageWithWarnings(Long projectId) throws IOException {
        Project project = projectRepository.findById(projectId).orElseThrow(()->
                new NoSuchElementException(String.format("Project with id %s is not found", projectId)));
        List<Locale> locales = new ArrayList<>();
        for(LocaleWrapper localeWrapper : project.getTargetLocales()){
            locales.add(localeWrapper.getLocale());
        }

        Workbook workbook = new XSSFWorkbook();
        for(Locale locale : locales) {
            List<MessageWithWarningsDTO> list = messageTranslationService.getMissingTranslation(projectId, locale.toString());
            int rowNum = 1;
            Sheet sheet = createSheet(workbook, locale.toString());
            for (MessageWithWarningsDTO message : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(message.getKey());
                row.createCell(1).setCellValue(message.getContent());
                row.createCell(2).setCellValue(message.getWarnings().getOutdatedTranslationContent());
                if (message.getWarnings().getSubstituteLocale() != null)
                    row.createCell(3).setCellValue(message.getWarnings().getSubstituteLocale().toString());
                row.createCell(4).setCellValue(message.getWarnings().getSubstituteContent());
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return new ByteArrayInputStream(bos.toByteArray());
    }

    private Sheet createSheet(Workbook workbook, String targetLocale){
        Sheet sheet = workbook.createSheet(targetLocale);
        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
        return sheet;
    }
}
