/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.report.excel;

import net.rrm.ehour.report.reports.element.LockableDate;
import net.rrm.ehour.ui.common.util.WebUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.wicket.model.IModel;

import java.util.Date;

/**
 * Created on Mar 25, 2009, 6:45:57 AM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class CellFactory {
    public static Cell createCell(Row row, int column, String value, ExcelWorkbook workbook) {
        return CellFactory.createCell(row, column, value, workbook, ExcelStyle.NORMAL_FONT);
    }

    public static Cell createCell(Row row, int column, IModel<String> valueModel, ExcelWorkbook workbook) {
        return CellFactory.createCell(row, column, valueModel, workbook, ExcelStyle.NORMAL_FONT);
    }

    public static Cell createCell(Row row, int column, ExcelWorkbook workbook, ExcelStyle excelStyle) {
        return createCell(row, column, "", workbook, excelStyle);
    }

    public static Cell createCell(Row row, int column, IModel<String> valueModel, ExcelWorkbook workbook, ExcelStyle excelStyle) {
        return createCell(row, column, WebUtils.getResourceModelString(valueModel), workbook, excelStyle);
    }

    public static Cell createCell(Row row, int column, Object value, ExcelWorkbook workbook, ExcelStyle excelStyle) {
        Cell cell = row.createCell(column);

        if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Date) {
            cell.setCellValue((Date)value);
        } else if (value instanceof LockableDate) {
            cell.setCellValue(((LockableDate) value).getDate());
        } else {
            cell.setCellValue(new XSSFRichTextString(value.toString()));
        }

        cell.setCellStyle(workbook.getCellStyle(excelStyle));

        return cell;
    }
}
