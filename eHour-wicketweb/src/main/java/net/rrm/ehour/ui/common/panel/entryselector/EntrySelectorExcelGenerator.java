package net.rrm.ehour.ui.common.panel.entryselector;

import net.rrm.ehour.ui.common.report.excel.ExcelWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.wicket.model.ResourceModel;

import java.io.Serializable;
import java.util.List;

public class EntrySelectorExcelGenerator {
    public ExcelWorkbook create(EntrySelectorData entrySelectorData, String sheetName) {
        ExcelWorkbook workbook = new ExcelWorkbook();
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));

        short rowIndex = createHeader(entrySelectorData, sheet, (short) 0);

        List<EntrySelectorData.EntrySelectorRow> rows = entrySelectorData.getRows();

        for (EntrySelectorData.EntrySelectorRow entrySelectorRow : rows) {
            Row row = sheet.createRow(rowIndex++);

            List<? extends Serializable> cells = entrySelectorRow.getCells();

            for (int columnIndex = 0; columnIndex < cells.size(); columnIndex++) {
                Cell cell = row.createCell(columnIndex);

                EntrySelectorData.Header header = entrySelectorData.getColumnHeaders().get(columnIndex);

                Serializable rawCellValue = cells.get(columnIndex);

                if (rawCellValue != null) {
                    String cellValue = rawCellValue.toString().replaceAll("&infin;", "infinite");

                    EntrySelectorData.ColumnType columnType = header.getColumnType();

                    if (columnType == EntrySelectorData.ColumnType.NUMERIC) {
                        cell.setCellValue(Float.parseFloat(cellValue));
                    } else {
                        cell.setCellValue(cellValue);
                    }
                }
            }
        }

        return workbook;
    }

    private short createHeader(EntrySelectorData entrySelectorData, Sheet sheet, short rowIndex) {
        Row row = sheet.createRow(rowIndex++);

        List<EntrySelectorData.Header> columnHeaders = entrySelectorData.getColumnHeaders();

        short columnIndex = 0;

        for (EntrySelectorData.Header columnHeader : columnHeaders) {
            Cell cell = row.createCell(columnIndex++);

            String resourceLabel = columnHeader.getResourceLabel();
            cell.setCellValue(new ResourceModel(resourceLabel).getObject());
        }

        return rowIndex;
    }
}
