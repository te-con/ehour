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

import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.ExcelWorkbook;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Created on Apr 12, 2009, 2:06:07 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportSignOff extends AbstractExportReportPart
{
	public ExportReportSignOff(int cellMargin, Sheet sheet, Report report, ExcelWorkbook workbook)
	{
		super(cellMargin, sheet, report, workbook);
	}

	@Override
	public int createPart(int rowNumber)
	{
		Sheet sheet = getSheet();
		ExcelWorkbook workbook = getWorkbook();
		int cellMargin = getCellMargin();
		
		Row row = sheet.createRow(rowNumber);
		
		createCustomerSignature(workbook, cellMargin, row);
		createUserSignature(workbook, cellMargin, row);

		rowNumber += 2;
		rowNumber = createSignOffBox(rowNumber);
		
		return rowNumber;
	}

	private void createCustomerSignature(ExcelWorkbook workbook, int cellMargin, Row row)
	{
		CellFactory.createCell(row, cellMargin, new ResourceModel("excelMonth.managerSignature"), workbook);
	}

	private void createUserSignature(ExcelWorkbook workbook, int cellMargin, Row row)
	{
		IModel<String> userSignature = new StringResourceModel("excelMonth.userSignature",
				null,
				new Object[]{EhourWebSession.getUser().getFullName()});

		
		CellFactory.createCell(row, cellMargin + 4, userSignature, workbook);
	}

	private int createSignOffBox(int rowNumber)
	{
		int cellMargin = getCellMargin();
		
		getSheet().addMergedRegion(new CellRangeAddress(rowNumber, rowNumber + 2, cellMargin, cellMargin + 2));
		getSheet().addMergedRegion(new CellRangeAddress(rowNumber, rowNumber + 2, cellMargin + 4, cellMargin + 6));

		// doesn't work properly, box is not around the whole merged cells
//		Row boxRow = getSheet().createRow(rowNumber);
//		CellFactory.createCell(boxRow, cellMargin, getWorkbook(), BORDER_THIN);
//		CellFactory.createCell(boxRow, cellMargin  + 4, getWorkbook(), BORDER_THIN);
		
		return rowNumber;
	}
}
