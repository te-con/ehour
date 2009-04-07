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

import static net.rrm.ehour.ui.common.report.excel.CellStyle.BOLD;
import static net.rrm.ehour.ui.common.report.excel.CellStyle.BORDER_SOUTH;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.CellFactory;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.wicket.model.ResourceModel;

/**
 * Created on Mar 25, 2009, 7:16:55 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportBodyHeader extends AbstractExportReportPart
{
	public ExportReportBodyHeader(int cellMargin, HSSFSheet sheet, Report report, HSSFWorkbook workbook)
	{
		super(cellMargin, sheet, report, workbook);
	}

	
	public int createPart(int rowNumber)
	{
		HSSFSheet sheet = getSheet();
		HSSFWorkbook workbook = getWorkbook();
		int cellMargin = getCellMargin();
		
		HSSFRow row = sheet.createRow(rowNumber++);
		
		CellFactory.createCell(row, cellMargin + 0, new ResourceModel("excelMonth.body.customer"), workbook, BOLD, BORDER_SOUTH);
		CellFactory.createCell(row, cellMargin + 1, new ResourceModel("excelMonth.body.project"), workbook, BOLD, BORDER_SOUTH);
		CellFactory.createCell(row, cellMargin + 2, new ResourceModel("excelMonth.body.date"), workbook, BOLD, BORDER_SOUTH);
		CellFactory.createCell(row, cellMargin + 3, workbook, BORDER_SOUTH);
		CellFactory.createCell(row, cellMargin + 4, workbook, BORDER_SOUTH);
		CellFactory.createCell(row, cellMargin + 5, workbook, BORDER_SOUTH);
		CellFactory.createCell(row, cellMargin + 6, new ResourceModel("excelMonth.body.hours"), workbook, BOLD, BORDER_SOUTH);
		
		return rowNumber;
	}
}
