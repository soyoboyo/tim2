package org.tim.constants;

public class CSVFileConstants {
	public static final String CSV_FILE_NAME = "translationReport.csv";
	public static final String[] STD_HEADERS = {"Locale", "Translation Status", "Translation", "Substitute Locale", "Substitute Translation"};
	public static final int EMPTY_LINES_BETWEEN_MESSAGES = 1;
	public static final int MESSAGE_LENGTH = 6;
	public static final int MESSAGE_BLOCK = MESSAGE_LENGTH + EMPTY_LINES_BETWEEN_MESSAGES;
	public static final int KEY_ROW = 0;
	public static final int KEY_COLUMN = 2;
	public static final int LOCALE_ROW = 4;
	public static final int LOCALE_COLUMN = 0;
	public static final int NEW_TRANSLATION_ROW = 5;
	public static final int NEW_TRANSLATION_COLUMN = 2;
	public static final int TRANSLATION_STATUS_ROW = 4;
	public static final int TRANSLATION_STATUS_COLUMN = 1;
}
