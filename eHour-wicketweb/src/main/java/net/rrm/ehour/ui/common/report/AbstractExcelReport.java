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

package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.ExcelStyle;
import net.rrm.ehour.ui.common.report.excel.ExcelWorkbook;
import net.rrm.ehour.ui.report.model.TreeReportElement;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Abstract aggregate excel report
 */
public abstract class AbstractExcelReport implements ExcelReport {
    private static final long serialVersionUID = 1L;

    private ReportConfig reportConfig;
    private IModel<ReportCriteria> reportCriteriaModel;

    public AbstractExcelReport(ReportConfig reportConfig, IModel<ReportCriteria> reportCriteriaModel) {
        this.reportConfig = reportConfig;
        this.reportCriteriaModel = reportCriteriaModel;
    }

    @Override
    public void write(OutputStream stream) throws IOException {
        ExcelWorkbook workbook = createWorkbook(createReport(reportCriteriaModel.getObject()));
        workbook.write(stream);
   }

    protected abstract Report createReport(ReportCriteria reportCriteria);

    /**
     * Create the workbook
     */
    protected ExcelWorkbook createWorkbook(Report treeReport) {
        ExcelWorkbook wb = new ExcelWorkbook();

        Sheet sheet = wb.createSheet(WorkbookUtil.createSafeSheetName(getExcelReportName().getObject()));
        int rowNumber = 0;
        short column;

        for (column = 0; column < 4; column++) {
            sheet.setColumnWidth(column, 5000);
        }

        for (; column < 7; column++) {
            sheet.setColumnWidth(column, 3000);
        }

        rowNumber = createHeaders(rowNumber, sheet, treeReport, wb);

        rowNumber = addColumnHeaders(rowNumber, sheet, wb);

        fillReportSheet(treeReport, sheet, rowNumber, wb);

        return wb;
    }

    protected abstract IModel<String> getExcelReportName();

    protected abstract IModel<String> getHeaderReportName();

    private int addColumnHeaders(int rowNumber, Sheet sheet, ExcelWorkbook workbook) {
        int cellNumber = 0;
        IModel<String> headerModel;

        Row row = sheet.createRow(rowNumber++);

        for (ReportColumn reportColumn : reportConfig.getReportColumns()) {
            if (reportColumn.isVisible()) {
                headerModel = new ResourceModel(reportColumn.getColumnHeaderResourceKey());

                CellFactory.createCell(row, cellNumber++, headerModel, workbook, ExcelStyle.HEADER);
            }
        }

        return rowNumber;
    }

    @SuppressWarnings("unchecked")
    protected void fillReportSheet(Report reportData, Sheet sheet, int rowNumber, ExcelWorkbook workbook) {
        List<TreeReportElement> matrix = (List<TreeReportElement>) reportData.getReportData().getReportElements();
        ReportColumn[] columnHeaders = reportConfig.getReportColumns();
        Row row;

        for (TreeReportElement element : matrix) {
            row = sheet.createRow(rowNumber++);

            addColumns(workbook, columnHeaders, row, element);
        }
    }

    private void addColumns(ExcelWorkbook workbook, ReportColumn[] columnHeaders, Row row, TreeReportElement element) {
        int i = 0;
        int cellNumber = 0;

        // add cells for a row
        for (Serializable cellValue : element.getRow()) {
            if (columnHeaders[i].isVisible()) {
                if (cellValue != null) {
                    switch (columnHeaders[i].getColumnType()) {
                        case HOUR:
                            CellFactory.createCell(row, cellNumber++, cellValue, workbook, ExcelStyle.DIGIT);
                            break;
                        case TURNOVER:
                        case RATE:
                            CellFactory.createCell(row, cellNumber++, cellValue, workbook, ExcelStyle.CURRENCY);
                            break;
                        case DATE:
                            CellFactory.createCell(row, cellNumber++, cellValue, workbook, ExcelStyle.DATE);
                            break;
                        default:
                            CellFactory.createCell(row, cellNumber++, cellValue, workbook, ExcelStyle.NORMAL_FONT);
                            break;
                    }
                } else {
                    cellNumber++;
                }
            }

            i++;
        }
    }

    @Override
    public String getFilenameWihoutSuffix() {
        return getExcelReportName().getObject().toLowerCase().replace(' ', '_');
    }


    protected int createHeaders(int rowNumber, Sheet sheet, Report report, ExcelWorkbook workbook) {
        Row row = sheet.createRow(rowNumber++);
        CellFactory.createCell(row, 0, getHeaderReportName(), workbook, ExcelStyle.BOLD_FONT);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        row = sheet.createRow(rowNumber++);
        CellFactory.createCell(row, 0, new ResourceModel("report.dateStart"), workbook, ExcelStyle.BOLD_FONT);

        if (report.getReportRange() == null ||
                report.getReportRange().getDateStart() == null) {
            CellFactory.createCell(row, 1, "--", workbook, ExcelStyle.BOLD_FONT);
        } else {
            CellFactory.createCell(row, 1, report.getReportCriteria().getReportRange().getDateStart(), workbook, ExcelStyle.BOLD_DATE);
        }

        CellFactory.createCell(row, 3, new ResourceModel("report.dateEnd"), workbook, ExcelStyle.BOLD_FONT);

        if (report.getReportRange() == null || report.getReportRange().getDateEnd() == null) {
            CellFactory.createCell(row, 4, "--", workbook, ExcelStyle.BOLD_FONT);
        } else {
            CellFactory.createCell(row, 4, report.getReportCriteria().getReportRange().getDateEnd(), workbook, ExcelStyle.BOLD_DATE);
        }

        rowNumber++;

        return rowNumber;
    }
}
