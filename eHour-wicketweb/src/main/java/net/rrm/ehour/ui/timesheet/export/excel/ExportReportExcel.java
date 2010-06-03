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

package net.rrm.ehour.ui.timesheet.export.excel;

import java.io.IOException;

import net.rrm.ehour.ui.common.component.AbstractExcelResource;
import net.rrm.ehour.ui.common.report.PoiUtil;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.timesheet.export.ExportCriteriaParameter;
import net.rrm.ehour.ui.timesheet.export.excel.part.ExportReportBody;
import net.rrm.ehour.ui.timesheet.export.excel.part.ExportReportBodyHeader;
import net.rrm.ehour.ui.timesheet.export.excel.part.ExportReportColumn;
import net.rrm.ehour.ui.timesheet.export.excel.part.ExportReportHeader;
import net.rrm.ehour.ui.timesheet.export.excel.part.ExportReportSignOff;
import net.rrm.ehour.ui.timesheet.export.excel.part.ExportReportTotal;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created on Mar 23, 2009, 1:30:04 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportExcel extends AbstractExcelResource
{
	private static final long serialVersionUID = -4841781257347819473L;
	private final static Logger LOGGER = Logger.getLogger(ExportReportExcel.class);
	
	private static final int CELL_BORDER = 1;
	
	public static String getId()
	{
		return "exportReportExcel";
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.service.ui.common.component.AbstractExcelResource#getExcelData(java.lang.String)
	 */
	@Override
	public byte[] getExcelData(Report report) throws IOException
	{
		LOGGER.trace("Creating excel report");
		HSSFWorkbook workbook = createWorkbook(report);

		return PoiUtil.getWorkbookAsBytes(workbook);
	}

	/**
	 * @param report
	 * @return
	 */
	private HSSFWorkbook createWorkbook(Report report)
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		HSSFSheet 	sheet = workbook.createSheet(CommonWebUtil.formatDate("MMMM", report.getReportRange().getDateStart()));

		sheet.autoSizeColumn((short) (CELL_BORDER + ExportReportColumn.DATE.getColumn()));
		sheet.autoSizeColumn((short) (CELL_BORDER + ExportReportColumn.PROJECT.getColumn()));
		sheet.autoSizeColumn((short) (CELL_BORDER + ExportReportColumn.CUSTOMER.getColumn()));
		sheet.autoSizeColumn((short) (CELL_BORDER + ExportReportColumn.HOURS.getColumn()));
		sheet.setColumnWidth(0, 1024);
		
		int rowNumber = 9;
		
		rowNumber = new ExportReportHeader(CELL_BORDER, sheet, report, workbook).createPart(rowNumber);
		rowNumber = new ExportReportBodyHeader(CELL_BORDER, sheet, report, workbook).createPart(rowNumber);
		rowNumber = new ExportReportBody(CELL_BORDER, sheet, report, workbook).createPart(rowNumber);
		rowNumber = new ExportReportTotal(CELL_BORDER, sheet, report, workbook).createPart(rowNumber);
		
		if (isInclSignOff(report))
		{
			rowNumber++;
			rowNumber = new ExportReportSignOff(CELL_BORDER, sheet, report, workbook).createPart(rowNumber);
		}
		
		return workbook;
	}
	
	private boolean isInclSignOff(Report report)
	{
		String key = ExportCriteriaParameter.INCL_SIGN_OFF.name();
		Object object = report.getReportCriteria().getUserCriteria().getCustomParameters().get(key);
		return (object == null) ? false : ((Boolean)object).booleanValue();
	}
	

	/* (non-Javadoc)
	 * @see net.rrm.ehour.service.ui.common.component.AbstractExcelResource#getFilename()
	 */
	@Override
	protected String getFilename()
	{
		return "month_report.xls";
	}
}
