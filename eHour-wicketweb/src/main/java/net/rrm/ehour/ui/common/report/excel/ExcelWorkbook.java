package net.rrm.ehour.ui.common.report.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelWorkbook {
    private static final String FONT_NAME = "Arial";

    private Map<ExcelStyle, CellStyle> pregeneratedStyles;

    private Workbook workbook;

    public ExcelWorkbook() {
        init();
    }

    private void init() {
        workbook = new XSSFWorkbook();

        pregenerateStyles(workbook);
    }
    private void pregenerateStyles(Workbook workbook) {
        pregeneratedStyles = new HashMap<>();

        ExcelStyle[] styles = ExcelStyle.values();

        Font font = workbook.createFont();
        font.setFontName(FONT_NAME);

        for (ExcelStyle style : styles) {
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);

            style.apply(workbook, cellStyle, font);

            pregeneratedStyles.put(style, cellStyle);
        }
    }

    public CellStyle getCellStyle(ExcelStyle forExcelStyle) {
        return pregeneratedStyles.get(forExcelStyle);
    }

    public Sheet createSheet(String sheetName) {
        return workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
    }

    public int addPicture(byte[] image, int imageType) {
        return workbook.addPicture(image, imageType);
    }

    public void write(OutputStream output) throws IOException {
        workbook.write(output);
    }

    public CreationHelper getCreationHelper() {
        return workbook.getCreationHelper();
    }

    public Workbook getWorkbook() {
        return workbook;
    }
}
