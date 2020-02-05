package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.constants.TranslationStatus;
import org.tim.constants.UserMessages;
import org.tim.entities.*;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.tim.constants.CSVFileConstants.CSV_FILE_NAME;
import static org.tim.constants.CSVFileConstants.STD_HEADERS;
import static org.tim.constants.UserMessages.*;


@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ProjectRepository projectRepository;
    private final MessageRepository messageRepository;
    private final TranslationRepository translationRepository;


    public String generateCSVReport(Long projectId, String[] localesForReport) {
        var project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));
        var messages = new LinkedList<>(messageRepository.findMessagesByProjectIdAndIsArchivedFalse(projectId));
        var reportData = new ArrayList<>(gatherReportData(localesForReport, messages, project));

        createCSVFile(reportData);
        return CSV_FILE_NAME;
    }

    private List<ReportDataRow> gatherReportData(String[] localesForReport, LinkedList<Message> messages, Project project) {
        var reportDataRows = new LinkedList<ReportDataRow>();
        for (var message : messages) {

            for (var localeString : localesForReport) {
                var row = new ReportDataRow();
                Locale locale;
                try {
                    locale = LocaleUtils.toLocale(localeString);
                } catch (IllegalArgumentException ex) {
                    throw new ValidationException(UserMessages.formatMessage(LCL_INVALID, localeString));
                }
                row.setMessage(message);
                row.setLocale(locale);
                Optional<Translation> translation;
                translation = translationRepository.findTranslationsByLocaleAndMessage(locale, message);
                setTranslationStatus(message, row, translation);
                if (row.getStatus() == TranslationStatus.Missing) {
                    SetSubstituteTranslation(project, message, locale, row);
                }
                reportDataRows.add(row);
            }
        }

        return reportDataRows;
    }

    private void SetSubstituteTranslation(Project project, Message message, Locale locale, ReportDataRow row) {

        while (true) {
            var subLocale = project.getSubstituteLocale(new LocaleWrapper(locale));
            Optional<Translation> subTranslation;
            if (subLocale.isPresent()) {
                subTranslation = translationRepository.findTranslationsByLocaleAndMessage(subLocale.get().getLocale(), message);
                locale = subLocale.get().getLocale();
                if (subTranslation.isPresent()) {
                    row.setSubstituteLocale(subLocale.get().getLocale());
                    row.setSubstituteTranslation(subTranslation.get().getContent());
                    return;
                }
            } else {
                return;
            }

        }

    }

    private void setTranslationStatus(Message message, ReportDataRow row, Optional<Translation> translation) {
        if (!translation.isPresent()) {
            row.setStatus(TranslationStatus.Missing);
            row.setTranslation(StringUtils.EMPTY);
            return;
        }

        if (message.isTranslationOutdated(translation.get())) {
            row.setStatus(TranslationStatus.Outdated);
            row.setTranslation(translation.get().getContent());
            return;
        }

        if (!translation.get().getIsValid()) {
            row.setStatus(TranslationStatus.Invalid);
            row.setTranslation(translation.get().getContent());
            return;
        }

        row.setStatus(TranslationStatus.Valid);
        row.setTranslation(translation.get().getContent());
        return;
    }


    private void createCSVFile(List<ReportDataRow> reportData) {
        try {
            Message currentMessage = null;
            CSVPrinter printer = new CSVPrinter(new FileWriter(CSV_FILE_NAME), CSVFormat.DEFAULT);

            try {
                for (var row : reportData) {
                    if (!row.getMessage().equals(currentMessage)) {
                        currentMessage = row.getMessage();
                        printMessageHeader(printer, row.getMessage());
                    }
                    if (row.getStatus() == TranslationStatus.Valid) {
                        continue;
                    }
                    if (row.getStatus() == TranslationStatus.Missing) {
                        printer.printRecord(row.Locale, row.status.name(), "-", row.getSubstituteLocale(), row.getSubstituteTranslation());
                    } else {
                        printer.printRecord(row.Locale, row.status.name(), row.translation, "-", "-");
                    }

                    printer.printRecord("New translation", "-");
                }
                printer.printRecord("");
            } catch (IOException ex) {
                throw new InvalidOperationException(CSV_WRITER_FAIL);
            }

            printer.close(true);
        } catch (IOException ex) {
            throw new InvalidOperationException(FILE_WRITER_FAIL);
        }
    }

    private void printMessageHeader(CSVPrinter printer, Message message) throws IOException {

        printer.printRecord("", "message Key", message.getKey());
        printer.printRecord("", "message Content", message.getContent());
        printer.printRecord("", "message Description", message.getDescription());
        printer.printRecord((Object[]) STD_HEADERS);
    }
}

