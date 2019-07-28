package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.constants.TranslationStatus;
import org.tim.entities.Message;
import org.tim.entities.ReportDataRow;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.tim.constants.CSVFileConstants.CSV_FILE_NAME;
import static org.tim.constants.CSVFileConstants.HEADERS;
import static org.tim.constants.UserMessages.CSV_WRITER_FAIL;
import static org.tim.constants.UserMessages.FILE_WRITER_FAIL;


@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ProjectRepository projectRepository;
    private final MessageRepository messageRepository;
    private final TranslationRepository translationRepository;


    public String generateCSVReport(Long projectId, String[] localesForReport) {
        projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));
        var messages = new LinkedList<>(messageRepository.findMessagesByProjectIdAndIsArchivedFalse(projectId));
        var reportData = new ArrayList<>(gatherReportData(localesForReport, messages));

        createCSVFile(reportData);
        return CSV_FILE_NAME;
    }

    private List<ReportDataRow> gatherReportData(String[] localesForReport, LinkedList<Message> messages) {
        var reportDataRows = new LinkedList<ReportDataRow>();
        for (var locale : localesForReport) {
            for (var message : messages) {
                var row = new ReportDataRow();
                row.setMessage(message.getContent());
                row.setLocale(locale);
                var translation = translationRepository.findTranslationsByLocaleAndMessage(new Locale(locale), message);
                reportDataRows.add(setTranslationStatus(message, row, translation));
            }
        }

        return reportDataRows;
    }

    private ReportDataRow setTranslationStatus(Message message, ReportDataRow row, Optional<Translation> translation) {
        if (!translation.isPresent()) {
            row.setStatus(TranslationStatus.Missing);
            row.setTranslation(StringUtils.EMPTY);
            return row;
        }

        if (message.isTranslationOutdated(translation.get())) {
            row.setStatus(TranslationStatus.Outdated);
            row.setTranslation(translation.get().getContent());
            return row;
        }

        if (!translation.get().getIsValid()) {
            row.setStatus(TranslationStatus.Invalid);
            row.setTranslation(translation.get().getContent());
            return row;
        }

        row.setStatus(TranslationStatus.Valid);
        row.setTranslation(translation.get().getContent());
        return row;
    }


    private void createCSVFile(List<ReportDataRow> reportData) {
        try {
            CSVPrinter printer = new CSVPrinter(new FileWriter(CSV_FILE_NAME), CSVFormat.DEFAULT.withHeader(HEADERS));
            reportData.forEach(row -> {
                try {
                    printer.printRecord(row.Locale, row.Message, row.Status.name(), row.Translation);
                } catch (IOException ex) {
                    throw new InvalidOperationException(CSV_WRITER_FAIL);
                }
            });
            printer.close(true);
        } catch (IOException ex) {
            throw new InvalidOperationException(FILE_WRITER_FAIL);
        }

    }
}

