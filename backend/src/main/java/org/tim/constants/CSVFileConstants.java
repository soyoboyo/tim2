package org.tim.constants;

public class CSVFileConstants {
	public final static String CSV_FILE_NAME = "translationReport.csv";
	public final static String[] STD_HEADERS = {"Locale", "Translation Status", "Translation", "Substitute Locale", "Substitute Translation"};

	public final static int EMPTY_LINES_BETWEEN_MESSAGES = 1;
	public final static int MESSAGE_LENGTH = 6;
	public final static int MESSAGE_BLOCK = MESSAGE_LENGTH + EMPTY_LINES_BETWEEN_MESSAGES;

	public final static int KEY_ROW = 0;
	public final static int KEY_COLUMN = 2;
	public final static int LOCALE_ROW = 4;
	public final static int LOCALE_COLUMN = 0;
	public final static int NEW_TRANSLATION_ROW = 5;
	public final static int NEW_TRANSLATION_COLUMN = 2;
	public final static int TRANSLATION_STATUS_ROW = 4;
	public final static int TRANSLATION_STATUS_COLUMN = 1;
}
