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

import static net.rrm.ehour.ui.common.report.excel.CellStyle.BORDER_NORTH;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.CellStyle;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.wicket.model.ResourceModel;

/**
 * Created on Mar 25, 2009, 6:40:48 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportTotal extends AbstractExportReportPart
{
	public ExportReportTotal(int cellMargin, HSSFSheet sheet, Report report, HSSFWorkbook workbook)
	{
		super(cellMargin, sheet, report, workbook);
	}
	
	@Override
	public int createPart(int rowNumber)
	{
		HSSFRow row = getSheet().createRow(rowNumber++);
		
		addTotalLabel(row);

		float total = getTotal();
		addTotalValue(total, row);

		createEmptyCells(row, BORDER_NORTH);
		CellFactory.createCell(row, getCellMargin() + ExportReportColumn.CUSTOMER.getColumn(), getWorkbook(), BORDER_NORTH);
		CellFactory.createCell(row, getCellMargin() + ExportReportColumn.PROJECT.getColumn(), getWorkbook(), BORDER_NORTH);
		
		return rowNumber;
	}

	private void addTotalValue(float total, HSSFRow row)
	{
		CellFactory.createCell(row, getCellMargin() + 6, total, getWorkbook(), CellStyle.DIGIT, CellStyle.BOLD, CellStyle.BORDER_NORTH);
	}
	
	private void addTotalLabel(HSSFRow row)
	{
		CellFactory.createCell(row, getCellMargin(), new ResourceModel("excelMonth.total"), getWorkbook(), CellStyle.BOLD, CellStyle.BORDER_NORTH);
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
