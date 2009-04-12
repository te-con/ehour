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

package net.rrm.ehour.ui.timesheet.export.excel.part;

import static net.rrm.ehour.ui.common.report.excel.CellStyle.BORDER_THIN;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.CellFactory;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.wicket.model.ResourceModel;

/**
 * Created on Apr 12, 2009, 2:06:07 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportSignOff extends AbstractExportReportPart
{
	public ExportReportSignOff(int cellMargin, HSSFSheet sheet, Report report, HSSFWorkbook workbook)
	{
		super(cellMargin, sheet, report, workbook);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.timesheet.export.excel.part.AbstractExportReportPart#createPart(int)
	 */
	@Override
	public int createPart(int rowNumber)
	{
		HSSFSheet sheet = getSheet();
		HSSFWorkbook workbook = getWorkbook();
		int cellMargin = getCellMargin();
		
		HSSFRow row = sheet.createRow(rowNumber);
		
//		System.out.println(getReport().getReportCriteria().getUserCriteria().getUsers().get(0));
		
		CellFactory.createCell(row, cellMargin, new ResourceModel("excelMonth.managerSignature"), workbook);
		CellFactory.createCell(row, cellMargin + 4, new ResourceModel("excelMonth.userSignature"), workbook);

		getSheet().addMergedRegion(new CellRangeAddress(rowNumber + 1, rowNumber + 4, cellMargin, cellMargin + 3));
		getSheet().addMergedRegion(new CellRangeAddress(rowNumber + 1, rowNumber + 4, cellMargin + 4, cellMargin + 6));
		
		HSSFRow boxRow = sheet.createRow(rowNumber + 1);
		
		CellFactory.createCell(boxRow, cellMargin, workbook, BORDER_THIN);
		
		
		return rowNumber;
	}
}
