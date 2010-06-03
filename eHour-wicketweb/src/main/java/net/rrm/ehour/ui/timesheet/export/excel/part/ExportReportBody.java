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

package net.rrm.ehour.ui.timesheet.export.excel.part;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.service.report.reports.ReportData;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.CellStyle;
import net.rrm.ehour.util.DateUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

/**
 * Created on Mar 25, 2009, 6:35:04 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
@SuppressWarnings("deprecation")
public class ExportReportBody extends AbstractExportReportPart
{
	public ExportReportBody(int cellMargin, HSSFSheet sheet, Report report, HSSFWorkbook workbook)
	{
		super(cellMargin, sheet, report, workbook);
	}
	
	@Override
	public int createPart(int rowNumber)
	{
		Map<Date, List<FlatReportElement>> dateMap = getElementsAsDateMap(getReport());
		List<Date> dateSequence = DateUtil.createDateSequence(getReport().getReportRange(), getConfig());
		
		rowNumber = createRowForDateSequence(rowNumber, dateMap, dateSequence);
		
		return rowNumber;
	}

	private int createRowForDateSequence(int rowNumber, Map<Date, List<FlatReportElement>> dateMap, List<Date> dateSequence)
	{
		for (Date date : dateSequence)
		{
			List<FlatReportElement> flatList = dateMap.get(date);
			
			boolean borderCells = isFirstDayOfWeek(date);
			
			if (!CollectionUtils.isEmpty(flatList))
			{
				rowNumber = addColumnsToRow(date, flatList, rowNumber, borderCells);
			}
			else
			{
				rowNumber = addEmptyRow(rowNumber, date, borderCells);
			}
		}
		return rowNumber;
	}
	
	private boolean isFirstDayOfWeek(Date date)
	{
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		
		return cal.get(Calendar.DAY_OF_WEEK) == getConfig().getFirstDayOfWeek();
	}

	private int addEmptyRow(int rowNumber, Date date, boolean borderCels)
	{
		HSSFRow row = getSheet().createRow(rowNumber++);
		HSSFCell dateCell = createDateCell(date, row);
		
		if (borderCels)
		{
			addThinNorthBorder(dateCell);
			
			CellStyle border = CellStyle.BORDER_NORTH_THIN;
			
			createEmptyCells(row, border);
			
			CellFactory.createCell(row, getCellMargin() +  ExportReportColumn.CUSTOMER.getColumn(), getWorkbook(), border);
			CellFactory.createCell(row, getCellMargin() +  ExportReportColumn.PROJECT.getColumn(), getWorkbook(), border);
			CellFactory.createCell(row, getCellMargin() +  ExportReportColumn.HOURS.getColumn(), getWorkbook(), border);
		}

		return rowNumber;
	}
	
	private int addColumnsToRow(Date date, List<FlatReportElement> elements, int rowNumber, boolean borderCells)
	{
		boolean addedForDate = false;
		
		for (FlatReportElement flatReportElement : elements)
		{
			HSSFRow row = getSheet().createRow(rowNumber);
			
			if (flatReportElement.getTotalHours() != null && flatReportElement.getTotalHours().doubleValue() > 0.0)
			{
				HSSFCell dateCell = createDateCell(date, row);
				HSSFCell projectCell = createProjectCell(flatReportElement.getProjectName(), row);
				HSSFCell hoursCell = createHoursCell(flatReportElement.getTotalHours(), row);
				HSSFCell customerCell = createCustomerCell(flatReportElement.getCustomerCode(), row);
				
				if (borderCells)
				{
					addThinNorthBorder(dateCell);
					addThinNorthBorder(projectCell);
					addThinNorthBorder(hoursCell);
					addThinNorthBorder(customerCell);
					
					createEmptyCells(row, CellStyle.BORDER_NORTH_THIN);
					
					getSheet().addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, getCellMargin() + 3, getCellMargin() + 5));
				}
				
				rowNumber++;
				addedForDate = true;
			}
		}
		
		if (!addedForDate)
		{
			HSSFRow row = getSheet().createRow(rowNumber++);
			HSSFCell datecell = createDateCell(date, row);	
			
			if (borderCells)
			{
				addThinNorthBorder(datecell);
			}
		}
		
		return rowNumber;
		
	}

	private void addThinNorthBorder(HSSFCell cell)
	{
		HSSFCellStyle cellStyle = cell.getCellStyle();
		CellStyle.BORDER_NORTH_THIN.getCellStyleElement().populate(cellStyle, getWorkbook());
	}
	
	private HSSFCell createHoursCell(Number hours, HSSFRow row)
	{
		return CellFactory.createCell(row, getCellMargin() + ExportReportColumn.HOURS.getColumn() ,hours,  getWorkbook(), CellStyle.DIGIT);
	}
	
	private HSSFCell createProjectCell(String project, HSSFRow row)
	{
		return CellFactory.createCell(row, getCellMargin() + ExportReportColumn.PROJECT.getColumn(), project, getWorkbook());
	}

	private HSSFCell createCustomerCell(String customerCode, HSSFRow row)
	{
		return CellFactory.createCell(row, getCellMargin() + ExportReportColumn.CUSTOMER.getColumn(), customerCode, getWorkbook());
	}

	
	private HSSFCell createDateCell(Date date, HSSFRow row)
	{
		return CellFactory.createCell(row, getCellMargin() + ExportReportColumn.DATE.getColumn() , getFormatter().format(date), getWorkbook(), CellStyle.DATE);
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
