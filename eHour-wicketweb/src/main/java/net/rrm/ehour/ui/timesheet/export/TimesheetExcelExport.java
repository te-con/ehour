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

package net.rrm.ehour.ui.timesheet.export;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.report.ExcelReport;
import net.rrm.ehour.ui.common.report.PoiUtil;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.ExcelWorkbook;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.ui.timesheet.export.excel.part.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.io.IOException;

/**
 * Created on Mar 23, 2009, 1:30:04 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class TimesheetExcelExport implements ExcelReport {
    private static final long serialVersionUID = -4841781257347819473L;

    private static final int CELL_BORDER = 1;

    private static final Logger LOGGER = Logger.getLogger(TimesheetExcelExport.class);

    @Override
    public byte[] getExcelData(ReportCriteria reportCriteria) {
        ExcelExportReportModel report = new ExcelExportReportModel(reportCriteria);
        ExcelWorkbook workbook = createWorkbook(report);

        try {
            return PoiUtil.getWorkbookAsBytes(workbook);
        } catch (IOException e) {
            LOGGER.warn(e);
            return new byte[0];
        }
    }

    private ExcelWorkbook createWorkbook(Report report) {
        ExcelWorkbook workbook = new ExcelWorkbook();

        HSSFSheet sheet = workbook.createSheet(WebUtils.formatDate("MMMM yyyy", report.getReportRange().getDateStart()));

        sheet.autoSizeColumn((short) (CELL_BORDER + ExportReportColumn.DATE.getColumn()));
        sheet.autoSizeColumn((short) (CELL_BORDER + ExportReportColumn.CUSTOMER_CODE.getColumn()));
        sheet.autoSizeColumn((short) (CELL_BORDER + ExportReportColumn.PROJECT.getColumn()));
        sheet.autoSizeColumn((short) (CELL_BORDER + ExportReportColumn.PROJECT_CODE.getColumn()));
        sheet.autoSizeColumn((short) (CELL_BORDER + ExportReportColumn.HOURS.getColumn()));
        sheet.setColumnWidth(0, 1024);

        int rowNumber = 9;

        rowNumber = new ExportReportHeader(CELL_BORDER, sheet, report, workbook).createPart(rowNumber);
        rowNumber = new ExportReportBodyHeader(CELL_BORDER, sheet, report, workbook).createPart(rowNumber);
        rowNumber = new ExportReportBody(CELL_BORDER, sheet, report, workbook).createPart(rowNumber);
        rowNumber = new ExportReportTotal(CELL_BORDER, sheet, report, workbook).createPart(rowNumber);

        if (isInclSignOff(report)) {
            new ExportReportSignOff(CELL_BORDER, sheet, report, workbook).createPart(rowNumber + 1);
        }

        return workbook;
    }

    private boolean isInclSignOff(Report report) {
        String key = TimesheetExportParameter.INCL_SIGN_OFF.name();
        Object object = report.getReportCriteria().getUserCriteria().getCustomParameters().get(key);
        return (object != null) && (Boolean) object;
    }

    @Override
    public String getFilename() {
        return "month_report.xls";
    }
}
