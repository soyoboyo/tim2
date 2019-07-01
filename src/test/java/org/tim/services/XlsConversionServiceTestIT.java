package org.tim.services;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Project;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XlsConversionServiceTestIT extends SpringTestsCustomExtension {

    @Autowired
    private XlsConversionService xlsConversionService;

    private Project project;

    @Test
    void whenAskingForXlsForMessageListThenXlsIsReturned() throws IOException {
        //given
        project = createEmptyGermanToEnglishAndFrenchProject();
        createTenRandomMessages(project);
        int messagesTotal = getMessagesCount();
        //when
        InputStream stream = xlsConversionService.getXlsForMessageWithWarnings(project.getId());
        //then
        Workbook workbook = new XSSFWorkbook(stream);
        Sheet sheetEN = workbook.getSheet(Locale.ENGLISH.toString());
        Sheet sheetFR = workbook.getSheet(Locale.FRENCH.toString());
        assertNotNull(sheetEN);
        assertEquals(messagesTotal, sheetEN.getLastRowNum());
        assertNotNull(sheetFR);
        assertEquals(messagesTotal, sheetFR.getLastRowNum());
    }
}
