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

import javax.servlet.http.HttpServletRequest;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.web.report.reports.AggregateReport;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Customer excel report 
 **/

public class CustomerExcelReport extends BaseExcelReportAction
{

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.web.report.excel.BaseExcelReportAction#createWorkbook(javax.servlet.http.HttpServletRequest, net.rrm.ehour.report.reports.ReportData)
	 */
	@Override
	public HSSFWorkbook createWorkbook(HttpServletRequest request, AggregateReport report)
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet 	sheet = wb.createSheet(getReportName());
		int			rowNumber = 0;
		
		createHeaders(sheet);
		rowNumber = createColumnNames(rowNumber, wb, sheet);
		createValues(rowNumber, wb, sheet, report);
		
		return wb;
	}

	private void createHeaders(HSSFSheet sheet)
	{
		
	}
	
	/**
	 * Create values
	 * @param rowNumber
	 * @param wb
	 * @param sheet
	 * @param reportData
	 * @return
	 */
	private int createValues(int rowNumber, HSSFWorkbook wb, HSSFSheet sheet, AggregateReport report)
	{
		HSSFRow		row;
		HSSFCell	cell;
		short		cellNumber = 0;

//		reportData.
		
		return rowNumber;
	}
	
	/**
	 * Create column names
	 * @param wb
	 * @param sheet
	 */
	private int createColumnNames(int rowNumber, HSSFWorkbook wb, HSSFSheet sheet)
	{
		HSSFRow		row;
		HSSFCell	cell;
		short		cellNumber = 0;
		HSSFCellStyle	headerStyle = this.getBoldCellStyle(wb);
		
		row = sheet.createRow(rowNumber++);
		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Customer");

		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Project");

		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Project code");

		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Employee");

		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Turnover");
		
		return rowNumber;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.web.report.excel.BaseExcelReportAction#getFilename()
	 */
	@Override
	protected String getReportName()
	{
		return "CustomerReport";
	}


}
