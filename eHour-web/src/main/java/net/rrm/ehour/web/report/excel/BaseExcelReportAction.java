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
import javax.servlet.http.HttpSession;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.web.report.action.ReUseReportAction;
import net.rrm.ehour.web.report.form.ReportForm;
import net.rrm.ehour.web.report.reports.AggregateReport;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public abstract class BaseExcelReportAction extends ReUseReportAction
{
	private final String	FONT_TYPE = "Arial";
	private HSSFFont		boldFont;
	protected HSSFCellStyle	boldStyle;

	protected AggregateReport getAggregateReport(HttpSession session,
			 String reportName,
			 ReportForm reportForm,
			 ReportData reportData)
	{
		return null;
	}
	
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
			
			workbook = createWorkbook(report);
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
	 * Get cellstyle with bold font
	 * @param workbook
	 * @return
	 */
	protected void initCellStyles(HSSFWorkbook workbook)
	{
		boldStyle = workbook.createCellStyle();
		
		boldFont = workbook.createFont();
		boldFont.setFontName(FONT_TYPE);
		boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		boldStyle.setFont(boldFont);
	}
	
	/**
	 * Create POI excel workbook
	 * @param request
	 * @param reportData
	 * @return
	 */
	public abstract HSSFWorkbook createWorkbook(AggregateReport report);
	
	/**
	 * Get report name
	 * @return
	 */
	protected abstract String getExcelReportName();
	
	
	/**
	 * Session data shouldn't be replaced as other charts on the same page
	 * could be created with the same session key
	 */
	protected boolean isReplaceSessionData()
	{
		return false;
	}	
	
}
