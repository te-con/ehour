/**
 * Created on Apr 22, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.web.report.excel;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.web.report.action.ReUseReportAction;
import net.rrm.ehour.web.report.form.ReportForm;
import net.rrm.ehour.web.report.reports.AggregateReport;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Base class for excel reporting which contains default cell styles and etc. and sets up the default header 
 **/

public abstract class BaseExcelReportAction extends ReUseReportAction
{
	private final String	FONT_TYPE = "Arial";
	private HSSFFont		boldFont;
	private HSSFFont		normalFont;
	protected HSSFCellStyle	boldCellStyle;
	protected HSSFCellStyle	headerCellStyle;
	protected HSSFCellStyle	valueDigitCellStyle;
	protected HSSFCellStyle	defaultCellStyle;
	protected HSSFCellStyle	currencyCellStyle;
	protected HSSFCellStyle	dateBoldCellStyle;
	
	
	/**
	 * 
	 */
	@Override
	protected ActionForward processReport(ActionMapping mapping,
											HttpServletResponse response,
											ReportForm reportForm,
											String reportName,
											ReportData reportData,
											AggregateReport report)
	{
		String			filename = getExcelReportName();
		HSSFWorkbook	workbook;
		OutputStream	outputStream;
		BufferedOutputStream bos;

		try
		{
			outputStream = response.getOutputStream();
			bos = new BufferedOutputStream(outputStream);			
			
			response.setContentType("application/x-ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
			
			workbook = createExcelReport(report);
			workbook.write(bos);
			bos.close();
			outputStream.close();
		}
		catch (IOException e)
		{
			logger.error("Can't write excel report to outputstream: " + e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Create excel report
	 * @param report
	 * @return
	 */

	private HSSFWorkbook createExcelReport(AggregateReport report)
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet 	sheet = wb.createSheet(getExcelReportName());
		int			rowNumber = 0;

		initCellStyles(wb);
		
		rowNumber = createHeaders(rowNumber, sheet, report);
		
		fillReportSheet(report, sheet, rowNumber);
		
		return wb;
	}
	
	/**
	 * Create header containing report date
	 * @param sheet
	 */

	private int createHeaders(int rowNumber, HSSFSheet sheet, AggregateReport report)
	{
		HSSFRow		row;
		HSSFCell	cell;

		row = sheet.createRow(rowNumber++);
		cell = row.createCell((short)0);
		cell.setCellStyle(boldCellStyle);
		// TODO i18n
		cell.setCellValue(getHeaderReportName());
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)1));

		row = sheet.createRow(rowNumber++);
		cell = row.createCell((short)0);
		cell.setCellStyle(boldCellStyle);
		cell.setCellValue("Start date:");

		cell = row.createCell((short)1);
		cell.setCellStyle(dateBoldCellStyle);
		cell.setCellValue(report.getReportCriteria().getReportRange().getDateStart());

		row = sheet.createRow(rowNumber++);
		cell = row.createCell((short)0);
		cell.setCellStyle(boldCellStyle);
		cell.setCellValue("End date:");

		cell = row.createCell((short)1);
		cell.setCellStyle(dateBoldCellStyle);
		cell.setCellValue(report.getReportCriteria().getReportRange().getDateEndForDisplay());
		
		rowNumber++;
		
		return rowNumber;
	}	
	
	/**
	 * Initialize cellstyles
	 * @param workbook
	 * @return
	 */
	protected void initCellStyles(HSSFWorkbook workbook)
	{
		headerCellStyle = workbook.createCellStyle();
		
		boldFont = workbook.createFont();
		boldFont.setFontName(FONT_TYPE);
		boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerCellStyle.setFont(boldFont);
		headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		headerCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);

		boldCellStyle = workbook.createCellStyle();
		boldCellStyle.setFont(boldFont);

		dateBoldCellStyle = workbook.createCellStyle();
		dateBoldCellStyle.setFont(boldFont);
		dateBoldCellStyle.setDataFormat((short)0xf);
		
		defaultCellStyle = workbook.createCellStyle();
		normalFont = workbook.createFont();
		normalFont.setFontName(FONT_TYPE);
		defaultCellStyle.setFont(normalFont);
		
		valueDigitCellStyle = workbook.createCellStyle();
		valueDigitCellStyle.setFont(normalFont);
		// 0.00 digit style
		valueDigitCellStyle.setDataFormat((short)2);

		currencyCellStyle= workbook.createCellStyle();
		currencyCellStyle.setFont(normalFont);
		currencyCellStyle.setDataFormat((short)0x7);
	
	}
	
	
	
	/**
	 * Fill report sheet
	 * @param request
	 * @param reportData
	 * @return
	 */
	protected abstract int fillReportSheet(AggregateReport report, HSSFSheet sheet, int rowNumber);
	
	/**
	 * Get report name for the filename
	 * @return
	 */
	protected abstract String getExcelReportName();
	
	/**
	 * Get report name for the header
	 * @return
	 */
	protected abstract String getHeaderReportName();
	
	/**
	 * Session data shouldn't be replaced as other charts on the same page
	 * could be created with the same session key
	 */
	protected boolean isReplaceSessionData()
	{
		return false;
	}
	
	
}
