package net.rrm.ehour.ui.common.report.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.WorkbookUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelWorkbook {
    private static final String FONT_NAME = "Arial";

    private Map<CellStyle, HSSFCellStyle> pregeneratedStyles;

    private HSSFWorkbook workbook;

    public ExcelWorkbook() {
        init();
    }

    private void init() {
        workbook = new HSSFWorkbook();

        pregenerateStyles(workbook);
    }
    private void pregenerateStyles(HSSFWorkbook workbook) {
        pregeneratedStyles = new HashMap<CellStyle, HSSFCellStyle>();

        CellStyle[] styleses = CellStyle.values();

        HSSFFont font = workbook.createFont();
        font.setFontName(FONT_NAME);

        for (CellStyle stylese : styleses) {
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);

            stylese.apply(workbook, cellStyle);

            pregeneratedStyles.put(stylese, cellStyle);
        }
    }

    public HSSFCellStyle getCellStyle(CellStyle forCellStyle) {
        return pregeneratedStyles.get(forCellStyle);
    }

    public HSSFSheet createSheet(String sheetName) {
        return workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));
    }

    public int addPicture(byte[] image, int imageType) {
        return workbook.addPicture(image, imageType);
    }

    public void write(OutputStream output) throws IOException {
        workbook.write(output);
    }
}
