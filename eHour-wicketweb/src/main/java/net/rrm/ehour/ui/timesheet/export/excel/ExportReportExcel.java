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

import java.io.IOException;

import net.rrm.ehour.ui.common.component.AbstractExcelResource;
import net.rrm.ehour.ui.common.report.ExcelWorkbook;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;

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
	 * @see net.rrm.ehour.ui.common.component.AbstractExcelResource#getExcelData(java.lang.String)
	 */
	@Override
	public byte[] getExcelData(String reportId) throws IOException
	{
		Report report = (Report)EhourWebSession.getSession().getObjectCache().getObjectFromCache(reportId);
		
		LOGGER.trace("Creating excel report");
		ExcelWorkbook workbook = createWorkbook(report);
		byte[] excelData = workbook.toByteArray();
		
		return excelData;
	}

	/**
	 * @param report
	 * @return
	 */
	private ExcelWorkbook createWorkbook(Report report)
	{
		ExcelWorkbook workbook = new ExcelWorkbook();
		
		HSSFSheet 	sheet = workbook.createSheet(CommonWebUtil.formatDate("MMMM", report.getReportRange().getDateStart()));

		int rowNumber = new ExportReportHeader(CELL_BORDER).createPart(11 -1, sheet, report, workbook);
		rowNumber++;
		rowNumber = new ExportReportBodyHeader(CELL_BORDER).createPart(rowNumber, sheet, report, workbook);
		rowNumber = new ExportReportBody(CELL_BORDER).createPart(rowNumber, sheet, report, workbook);
		rowNumber++;
		rowNumber = new ExportReportTotal(CELL_BORDER).createPart(rowNumber, sheet, report, workbook);
		return workbook;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.component.AbstractExcelResource#getFilename()
	 */
	@Override
	protected String getFilename()
	{
		// TODO Auto-generated method stub
		return "TODO.xls";
	}
}
