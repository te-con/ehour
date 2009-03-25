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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.report.ExcelWorkbook;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.ExcelWorkbook.StyleType;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.PoiUtil;
import net.rrm.ehour.util.DateUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Created on Mar 25, 2009, 6:35:04 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportBody
{
	private int cellMargin;
	
	public ExportReportBody(int cellMargin)
	{
		this.cellMargin = cellMargin;
	}
	
	public int createPart(int rowNumber, HSSFSheet sheet, Report report, ExcelWorkbook workbook)
	{
		Map<Date, List<FlatReportElement>> dateMap = getElementsAsDateMap(report);
	
		EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		Locale locale = config.getLocale();
		DateFormat formatter = new SimpleDateFormat("dd MMM yy", locale);
		
		List<Date> dateSequence = DateUtil.createDateSequence(report.getReportRange(), config);
		
		for (Date date : dateSequence)
		{
			List<FlatReportElement> flatList = dateMap.get(date);
			
			if (!CollectionUtils.isEmpty(flatList))
			{
				rowNumber = addElementsForDate(date, flatList, rowNumber, sheet, workbook, formatter);
			}
			else
			{
				rowNumber = addBodyEmptyRow(rowNumber, sheet, workbook, formatter, date);
			}
		}
		
		return rowNumber;
	}

	private int addBodyEmptyRow(int rowNumber, HSSFSheet sheet, ExcelWorkbook workbook, DateFormat formatter, Date date)
	{
		HSSFRow row = sheet.createRow(rowNumber++);
		addBodyDate(date, row, workbook, formatter);
		return rowNumber;
	}
	
	private int addElementsForDate(Date date, List<FlatReportElement> elements, int rowNumber, HSSFSheet sheet, ExcelWorkbook workbook, DateFormat formatter)
	{
		boolean addedForDate = false;
		
		for (FlatReportElement flatReportElement : elements)
		{
			HSSFRow row = sheet.createRow(rowNumber++);
			
			if (flatReportElement.getTotalHours() != null && flatReportElement.getTotalHours().doubleValue() > 0.0)
			{
				addBodyDate(date, row, workbook, formatter);
				addBodyProject(flatReportElement.getProjectName(), row, workbook);
				addBodyHours(flatReportElement.getTotalHours(), row, workbook);
				
				addedForDate = true;
			}
		}
		
		if (!addedForDate)
		{
			HSSFRow row = sheet.createRow(rowNumber++);
			addBodyDate(date, row, workbook, formatter);	
		}
		
		return rowNumber;
		
	}

	private void addBodyHours(Number hours, HSSFRow row, ExcelWorkbook workbook)
	{
		PoiUtil.createCell(row, cellMargin + 6 ,hours.floatValue(), StyleType.VALUE_DIGIT, workbook);
	}

	
	private void addBodyProject(String project, HSSFRow row, ExcelWorkbook workbook)
	{
		PoiUtil.createCell(row, cellMargin, project, workbook);
	}

	
	private void addBodyDate(Date date, HSSFRow row, ExcelWorkbook workbook, DateFormat formatter)
	{
		PoiUtil.createCell(row, cellMargin + 2 , formatter.format(date), StyleType.DATE_NORMAL, workbook);
	}
	
	/**
	 * Return a map with the key being the report's date and a list of a report elements for that date as the value
	 * @param report
	 * @return
	 */
	private Map<Date, List<FlatReportElement>> getElementsAsDateMap(Report report)
	{
		Map<Date, List<FlatReportElement>> flatMap = new TreeMap<Date, List<FlatReportElement>>();
		
		ReportData reportData = report.getReportData();
		
		for (ReportElement reportElement : reportData.getReportElements())
		{
			FlatReportElement flat = (FlatReportElement)reportElement;
		
			Date date = DateUtil.nullifyTime(flat.getDayDate());
			
			List<FlatReportElement> dateElements;
			
			if (flatMap.containsKey(date))
			{
				dateElements = flatMap.get(date);
			}
			else
			{
				dateElements = new ArrayList<FlatReportElement>();
			}
			
			dateElements.add(flat);
			
			flatMap.put(date, dateElements);
		}
		
		return flatMap;
	}	
}
