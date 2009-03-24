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
import java.text.SimpleDateFormat;
import java.util.Locale;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.ui.common.component.AbstractExcelResource;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.report.ExcelWorkbook;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.ExcelWorkbook.StyleType;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Created on Mar 23, 2009, 1:30:04 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportExcel extends AbstractExcelResource
{
	private static final long serialVersionUID = -4841781257347819473L;
	private final static Logger LOGGER = Logger.getLogger(ExportReportExcel.class);
	
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
		
		HSSFSheet 	sheet = workbook.createSheet(getShortName(report.getReportRange()));

		int rowNumber = createHeaders(11, sheet, report, workbook);
		
		return workbook;
	}
	
	private int createHeaders(int rowNumber, HSSFSheet sheet, Report report, ExcelWorkbook workbook)
	{
		rowNumber = addReportTitle(rowNumber, sheet, report, workbook);
		return rowNumber;
	}

	/**
	 * @param rowNumber
	 * @param sheet
	 * @param report
	 * @param workbook
	 * @return
	 */
	private int addReportTitle(int rowNumber, HSSFSheet sheet, Report report, ExcelWorkbook workbook)
	{
		HSSFRow row = sheet.createRow(rowNumber++);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(workbook.getCellStyle(StyleType.BOLD));
		cell.setCellValue(new HSSFRichTextString(CommonWebUtil.getResourceModelString(getExcelReportName(report.getReportRange()))));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 0));
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
		
		System.out.println(CommonWebUtil.getResourceModelString(title));
		
		return title;
	}
	
	private String getShortName(DateRange dateRange)
	{
		Locale locale = EhourWebSession.getSession().getEhourConfig().getLocale();
		
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", locale);
		
		return formatter.format(dateRange.getDateStart());
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.component.AbstractExcelResource#getFilename()
	 */
	@Override
	protected String getFilename()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
