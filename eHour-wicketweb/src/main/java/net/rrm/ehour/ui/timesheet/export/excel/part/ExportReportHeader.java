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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.CellStyle;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Created on Mar 25, 2009, 6:37:20 AM
 * 
 * @author Thies Edeling (thies@te-con.nl)
 * 
 */
public class ExportReportHeader extends AbstractExportReportPart
{
	public ExportReportHeader(int cellMargin, HSSFSheet sheet, Report report, HSSFWorkbook workbook)
	{
		super(cellMargin, sheet, report, workbook);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.timesheet.export.excel.part.AbstractExportReportPart#createPart(int)
	 */
	@Override
	public int createPart(int rowNumber)
	{
		rowNumber = addTitleRow(rowNumber);
		rowNumber = addTitleDateRow(rowNumber);
		rowNumber++;

		return rowNumber;
	}

	private int addTitleRow(int rowNumber)
	{
		HSSFRow row = getSheet().createRow(rowNumber++);
		
		CellFactory.createCell(row, getCellMargin(), getExcelReportName(getReport().getReportRange()), getWorkbook(), CellStyle.BOLD);
//		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 0));
		return rowNumber;
	}

	private int addLogo(int rowNumber)
	{
		
		
		return rowNumber;
	}
	
	
	private int addTitleDateRow(int rowNumber)
	{
		HSSFRow row = getSheet().createRow(rowNumber++);
		
		CellFactory.createCell(row, getCellMargin(), new ResourceModel("excelMonth.date"), getWorkbook(), CellStyle.NORMAL);
		CellFactory.createCell(row, getCellMargin() + 2, CommonWebUtil.formatDate("MMMM yyyy", getReport().getReportRange().getDateStart()), getWorkbook(), CellStyle.NORMAL);

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
