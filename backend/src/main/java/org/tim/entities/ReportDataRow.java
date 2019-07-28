package org.tim.entities;

import lombok.Data;
import org.tim.constants.TranslationStatus;


@Data
public class ReportDataRow {

    public String Locale;
    public String Message;
    public String Translation;
    public TranslationStatus Status;

}
