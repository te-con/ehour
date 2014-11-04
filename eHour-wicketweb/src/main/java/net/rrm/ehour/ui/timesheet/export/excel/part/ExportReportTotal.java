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

package net.rrm.ehour.ui.timesheet.export.excel.part;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.ExcelStyle;
import net.rrm.ehour.ui.common.report.excel.ExcelWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.wicket.model.ResourceModel;

/**
 * Created on Mar 25, 2009, 6:40:48 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportTotal extends AbstractExportReportPart
{
	public ExportReportTotal(int cellMargin, Sheet sheet, Report report, ExcelWorkbook workbook)
	{
		super(cellMargin, sheet, report, workbook);
	}
	
	@Override
	public int createPart(int rowNumber)
	{
		Row row = getSheet().createRow(rowNumber++);
		
		addTotalLabel(row);

		float total = getTotal();
		addTotalValue(total, row);


        createEmptyCells(row, ExcelStyle.BORDER_NORTH);
		CellFactory.createCell(row, getCellMargin() + ExportReportColumn.CUSTOMER_CODE.getColumn(), getWorkbook(), ExcelStyle.BORDER_NORTH);
		CellFactory.createCell(row, getCellMargin() + ExportReportColumn.PROJECT.getColumn(), getWorkbook(), ExcelStyle.BORDER_NORTH);
        CellFactory.createCell(row, getCellMargin() + ExportReportColumn.PROJECT_CODE.getColumn(), getWorkbook(), ExcelStyle.BORDER_NORTH);
		
		return rowNumber;
	}

	private void addTotalValue(float total, Row row) {
        CellFactory.createCell(row, getCellMargin() + 6, total, getWorkbook(), ExcelStyle.DIGIT_BOLD_BORDER_NORTH);
    }

    private void addTotalLabel(Row row)
	{
		CellFactory.createCell(row, getCellMargin(), new ResourceModel("excelMonth.total"), getWorkbook(), ExcelStyle.BOLD_BORDER_NORTH);
	}
	
	private float getTotal()
	{
		float total = 0; 
		
		ReportData reportData = getReport().getReportData();
		
		for (ReportElement reportElement : reportData.getReportElements())
		{
			FlatReportElement flat = (FlatReportElement)reportElement;
			
			if (flat.getTotalHours() != null)
			{
				total += flat.getTotalHours().floatValue();
			}
		}
		
		return total;
	}
}
