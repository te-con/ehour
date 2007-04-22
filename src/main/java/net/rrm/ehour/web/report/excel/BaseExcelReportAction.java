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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.web.report.action.reuse.ReUseAggregateReportAction;
import net.rrm.ehour.web.report.form.ReportChartForm;
import net.rrm.ehour.web.report.reports.AggregateReport;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public abstract class BaseExcelReportAction extends ReUseAggregateReportAction
{
	private final String	FONT_TYPE = "Arial";
	private HSSFFont		boldFont;
	
	/**
	 * 
	 */
	@Override
	protected ActionForward reUseReport(ActionMapping mapping, HttpServletRequest request,
										HttpServletResponse response, ReportChartForm chartForm, 
										AggregateReport report) throws IOException
	{
		String			filename = getReportName();
		HSSFWorkbook	workbook;
		OutputStream	outputStream;
		BufferedOutputStream bos;

		outputStream = response.getOutputStream();
		bos = new BufferedOutputStream(outputStream);			
		
		response.setContentType("application/x-ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
		
		workbook = createWorkbook(request, report);
		workbook.write(bos);
		bos.close();
		outputStream.close();
		
		return null;
	}
	
	/**
	 * Get cellstyle with bold font
	 * @param workbook
	 * @return
	 */
	protected HSSFCellStyle getBoldCellStyle(HSSFWorkbook workbook)
	{
		HSSFCellStyle 	style = workbook.createCellStyle();
		
		if (boldFont == null)
		{
			boldFont = workbook.createFont();
			boldFont.setFontName(FONT_TYPE);
			boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}		
		
		style.setFont(boldFont);
		
		return style;
	}
	
	/**
	 * Create POI excel workbook
	 * @param request
	 * @param reportData
	 * @return
	 */
	public abstract HSSFWorkbook createWorkbook(HttpServletRequest request, AggregateReport report);
	
	/**
	 * Get report name
	 * @return
	 */
	protected abstract String getReportName();
	
}
