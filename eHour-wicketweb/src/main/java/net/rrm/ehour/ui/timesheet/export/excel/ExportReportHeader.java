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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.report.ExcelWorkbook;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.ExcelWorkbook.CellStyle;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Created on Mar 25, 2009, 6:37:20 AM
 * 
 * @author Thies Edeling (thies@te-con.nl)
 * 
 */
public class ExportReportHeader
{
	private int cellMargin;

	public ExportReportHeader(int cellMargin)
	{
		this.cellMargin = cellMargin;
	}

	public int createPart(int rowNumber, HSSFSheet sheet, Report report, ExcelWorkbook workbook)
	{
		rowNumber = addTitleRow(rowNumber, sheet, report, workbook);
		rowNumber = addTitleDateRow(rowNumber, sheet, report, workbook);
		rowNumber++;

		return rowNumber;
	}

	private int addTitleRow(int rowNumber, HSSFSheet sheet, Report report, ExcelWorkbook workbook)
	{
		HSSFRow row = sheet.createRow(rowNumber++);
		HSSFCell cell = row.createCell(cellMargin);
		cell.setCellStyle(workbook.getCellStyle(CellStyle.BOLD));
		cell.setCellValue(new HSSFRichTextString(CommonWebUtil.getResourceModelString(getExcelReportName(report.getReportRange()))));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 0));
		return rowNumber;
	}

	private int addTitleDateRow(int rowNumber, HSSFSheet sheet, Report report, ExcelWorkbook workbook)
	{
		HSSFRow row = sheet.createRow(rowNumber++);
		HSSFCell cell = row.createCell(cellMargin);
		cell.setCellStyle(workbook.getCellStyle(CellStyle.NORMAL));
		cell.setCellValue(new HSSFRichTextString(CommonWebUtil.getResourceModelString(new ResourceModel("excelMonth.date"))));

		HSSFCell dataCell = row.createCell(cellMargin + 2);
		dataCell.setCellStyle(workbook.getCellStyle(CellStyle.NORMAL));
		dataCell.setCellValue(new HSSFRichTextString(CommonWebUtil.formatDate("MMMM yyyy", report.getReportRange().getDateStart())));

		return rowNumber;
	}
	
	private IModel getExcelReportName(DateRange dateRange)
	{
		EhourWebSession session = EhourWebSession.getSession();
		EhourConfig config = session.getEhourConfig();
		
		IModel title = new StringResourceModel("excelMonth.reportName",
				null,
				new Object[]{session.getUser().getUser().getFullName(),
							 new DateModel(dateRange.getDateStart() , config, DateModel.DATESTYLE_MONTHONLY)});
		
		return title;
	}	
}
