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
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.ExcelWorkbook;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.ui.timesheet.export.excel.part.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created on Mar 23, 2009, 1:30:04 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class TimesheetExcelExport implements ExcelReport {
    private static final long serialVersionUID = -4841781257347819473L;

    private static final int CELL_BORDER = 1;
    private final ReportCriteria reportCriteria;

    public TimesheetExcelExport(ReportCriteria reportCriteria) {
        this.reportCriteria = reportCriteria;
    }

    @Override
    public void write(OutputStream stream) throws IOException {
        ExcelExportReportModel report = new ExcelExportReportModel(reportCriteria);
        ExcelWorkbook workbook = createWorkbook(report);

        workbook.write(stream);
    }

    private ExcelWorkbook createWorkbook(Report report) {
        ExcelWorkbook workbook = new ExcelWorkbook();

        String sheetName = WebUtils.formatDate("MMMM yyyy", report.getReportRange().getDateStart());
        Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName));

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
        Object object = report.getReportCriteria().getUserSelectedCriteria().getCustomParameters().get(key);
        return (object != null) && (Boolean) object;
    }

    @Override
    public String getFilenameWihoutSuffix() {
        return "month_report";
    }
}
