/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.timesheet.export.excel;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.report.ExcelWorkbook;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.ExcelWorkbook.StyleType;
import net.rrm.ehour.ui.common.util.PoiUtil;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.wicket.model.ResourceModel;

/**
 * Created on Mar 25, 2009, 6:40:48 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportTotal
{
	private int cellMargin;

	public ExportReportTotal(int cellMargin)
	{
		this.cellMargin = cellMargin;
	}
	
	public int createPart(int rowNumber, HSSFSheet sheet, Report report, ExcelWorkbook workbook)
	{
		HSSFRow row = sheet.createRow(rowNumber++);
		
		addTotalLabel(row, workbook);

		float total = getTotal(report);
		addTotalValue(total, row, workbook);
		
		return rowNumber;
	}

	private void addTotalValue(float total, HSSFRow row, ExcelWorkbook workbook)
	{
		PoiUtil.createCell(row, cellMargin + 6, total, StyleType.VALUE_DIGIT, workbook);
	}

	
	private void addTotalLabel(HSSFRow row, ExcelWorkbook workbook)
	{
		PoiUtil.createCell(row, cellMargin, new ResourceModel("excelMonth.total"), StyleType.BOLD, workbook);
	}
	
	private float getTotal(Report report)
	{
		float total = 0; 
		
		ReportData reportData = report.getReportData();
		
		for (ReportElement reportElement : reportData.getReportElements())
		{
			FlatReportElement flat = (FlatReportElement)reportElement;
			
			total += flat.getTotalHours().floatValue();
		}
		
		return total;
	}
	
	
}
