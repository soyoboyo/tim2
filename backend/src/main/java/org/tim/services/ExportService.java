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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.tim.constants.CSVFileConstants.CSV_FILE_NAME;
import static org.tim.constants.CSVFileConstants.STD_HEADERS;
import static org.tim.constants.UserMessages.*;


@Service
@Transactional
@RequiredArgsConstructor
public class ExportService {

	private final ProjectRepository projectRepository;
	private final MessageRepository messageRepository;
	private final TranslationRepository translationRepository;


	public byte[] generateCSVReport(Long projectId, String[] localesForReport) throws IOException {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));
		List<Message> messages = new LinkedList<>(messageRepository.findMessagesByProjectIdAndIsArchivedFalse(projectId));
		List<ReportDataRow> reportData = new ArrayList<>(gatherReportData(localesForReport, messages, project));
		return createCSVFile(reportData);
	}

	private List<ReportDataRow> gatherReportData(String[] localesForReport, List<Message> messages, Project project) {
		List<ReportDataRow> reportDataRows = new LinkedList<>();
		for (Message message : messages) {
			for (String localeString : localesForReport) {
				ReportDataRow row = new ReportDataRow();
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
					setSubstituteTranslation(project, message, locale, row);
				}
				reportDataRows.add(row);
			}
		}
		return reportDataRows;
	}

	private void setSubstituteTranslation(Project project, Message message, Locale locale, ReportDataRow row) {
		while (true) {
			Optional<LocaleWrapper> subLocale = project.getSubstituteLocale(new LocaleWrapper(locale));
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
		if (translation.isEmpty()) {
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
	}


	private byte[] createCSVFile(List<ReportDataRow> reportData) throws IOException {
		File csvFile;
		try {
			csvFile = new File(CSV_FILE_NAME);
			CSVPrinter printer = new CSVPrinter(new FileWriter(csvFile), CSVFormat.DEFAULT);
			boolean shouldPrintMessageHeader, isNextMessage;
			ReportDataRow previousData = null;
			try {
				for (ReportDataRow row : reportData) {
					if (!row.getStatus().equals(TranslationStatus.Valid)) {
						isNextMessage = checkIfRowIsNextMessage(row, previousData);
						shouldPrintMessageHeader = checkIfPrintMessageHeader(row, previousData);
						if (isNextMessage) {
							printer.println();
						}
						if (shouldPrintMessageHeader) {
							printMessageHeader(printer, row.getMessage());
						}
						printer.printRecord(row.Locale, row.status.name(), row.getTranslation(), row.getSubstituteLocale(), row.getSubstituteTranslation());
						printer.printRecord("", "New translation", "");

						if (!Objects.equals(previousData, row)) {
							previousData = row;
						}
					}
				}
				printer.printRecord("");
			} catch (IOException ex) {
				throw new InvalidOperationException(CSV_WRITER_FAIL);
			} finally {
				printer.close(true);
			}
		} catch (IOException ex) {
			throw new InvalidOperationException(FILE_WRITER_FAIL);
		}

		byte[] bytes = Files.readAllBytes(csvFile.toPath());
		csvFile.delete();
		return bytes;
	}

	private boolean checkIfPrintMessageHeader(ReportDataRow row, ReportDataRow previousData) {
		if (previousData == null) {
			return true;
		}
		return !row.getMessage().getKey().equals(previousData.getMessage().getKey());
	}

	private boolean checkIfRowIsNextMessage(ReportDataRow row, ReportDataRow previousData) {
		if (previousData == null) {
			return false;
		}
		return !previousData.getMessage().getKey().equals(row.getMessage().getKey());
	}

	private void printMessageHeader(CSVPrinter printer, Message message) throws IOException {
		printer.printRecord("", "Key", message.getKey());
		printer.printRecord("", "Content", message.getContent());
		printer.printRecord("", "Description", message.getDescription());
		printer.printRecord("", "Last updated", message.getUpdateDate());
		printer.printRecord(STD_HEADERS);
	}
}

