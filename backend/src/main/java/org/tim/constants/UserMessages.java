package org.tim.constants;

import java.text.MessageFormat;

public class UserMessages {
    public static final String LCL_NOT_IN_TARGET = "{0} locale is not part of this project's target locales.";
    public static final String LCL_CYCLES = "This replacement would form cyclical relationship between locales.";
    public static final String LCL_NOT_FOUND = "Locale wrapper {0} not found in target locales.";
    public static final String LANG_NOT_FOUND_IN_PROJ = "Sorry, but the language of this translation does not match the expected language";
    public static final String FILE_WRITER_FAIL = "IOException during file writer creation.";
    public static final String CSV_WRITER_FAIL = "IOException during csv file creation.";

    public static String formatMessage(String messageString, Object arg0) {
        Object[] args = new Object[1];
        args[0] = arg0;
        return MessageFormat.format(messageString, args);
    }
}
