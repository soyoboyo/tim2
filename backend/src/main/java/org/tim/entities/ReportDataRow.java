package org.tim.entities;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.tim.constants.TranslationStatus;

import java.util.Locale;


@Data
public class ReportDataRow {

    public java.util.Locale Locale;
    public Message Message;
    public String Translation;
    public TranslationStatus Status;
    public String SubstituteTranslation;
    public java.util.Locale SubstituteLocale;

    public String getSubstituteTranslation(){
        if(SubstituteTranslation == null){
            return StringUtils.EMPTY;
        }
        return SubstituteTranslation;
    }
    public String getSubstituteLocale(){
        if(SubstituteLocale == null){
            return StringUtils.EMPTY;
        }
        return SubstituteLocale.toString();
    }
}
