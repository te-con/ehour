/**
 * Created on May 1, 2007
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

import java.util.Set;
import java.util.SortedMap;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.web.report.reports.AggregateReport;
import net.rrm.ehour.web.report.reports.ProjectReport;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Project excel report 
 **/

public class ProjectExcelReport extends BaseExcelReportAction
{
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.web.report.excel.BaseExcelReportAction#fillReportSheet(net.rrm.ehour.web.report.reports.AggregateReport, org.apache.poi.hssf.usermodel.HSSFSheet, int)
	 */
	@Override
	protected int fillReportSheet(AggregateReport report, HSSFSheet sheet, int rowNumber)
	{
		ProjectReport projectReport = (ProjectReport)report;
		
		rowNumber = createColumnNames(rowNumber, sheet);
		rowNumber = createValues(rowNumber, sheet, projectReport);
		
		return rowNumber;
	}
	
	/**
	 * Create values
	 * @param rowNumber
	 * @param wb
	 * @param sheet
	 * @param reportData
	 * @return
	 */
	private int createValues(int rowNumber, HSSFSheet sheet, ProjectReport report)
	{
		HSSFRow		row;
		HSSFCell	cell;
		short		cellNumber = 0;

		Set<Project>	projects = report.getReportValues().keySet();
		SortedMap<Customer, Set<ProjectAssignmentAggregate>>	customerPerProjects;
		Set<ProjectAssignmentAggregate>	aggregates;
		
		for (Project prj : projects)
		{
			customerPerProjects = report.getReportValues().get(prj);
			
			for (Customer cust : customerPerProjects.keySet())
			{
				aggregates = customerPerProjects.get(cust);
				
				for (ProjectAssignmentAggregate aggregate : aggregates)
				{
					row = sheet.createRow(rowNumber++);

					// project name
					cell = row.createCell(cellNumber++);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(prj.getName());

					// project code
					cell = row.createCell(cellNumber++);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(prj.getProjectCode());

					// customer name
					cell = row.createCell(cellNumber++);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(cust.getName());


					// employee name
					cell = row.createCell(cellNumber++);
					cell.setCellStyle(defaultCellStyle);
					cell.setCellValue(aggregate.getProjectAssignment().getUser().getLastName() + ", " +
							aggregate.getProjectAssignment().getUser().getFirstName());
					
					// aggregated hours
					cell = row.createCell(cellNumber++);
					cell.setCellStyle(valueDigitCellStyle);
					if (aggregate.getHours() != null)
					{
						// TODO round it to 2 digits
						cell.setCellValue(aggregate.getHours().floatValue());
					}
					
					// hourly rate
					cell = row.createCell(cellNumber++);
					cell.setCellStyle(currencyCellStyle);
					
					if (aggregate.getProjectAssignment().getHourlyRate() != null)
					{
						cell.setCellValue(aggregate.getProjectAssignment().getHourlyRate().floatValue());
					}

					// turnover
					cell = row.createCell(cellNumber++);
					cell.setCellStyle(currencyCellStyle);
					cell.setCellFormula("E" + rowNumber + "*F" + rowNumber);
//					if (aggregate.getTurnOver() != null)
//					{
//						cell.setCellValue(aggregate.getTurnOver().floatValue());
//					}
					
					cellNumber = 0;
				}
			}
		}
		
		return rowNumber;
	}	

	/**
	 * Create column names
	 * TODO i18n
	 * @param wb
	 * @param sheet
	 */
	private int createColumnNames(int rowNumber, HSSFSheet sheet)
	{
		HSSFRow		row;
		HSSFCell	cell;
		short		cellNumber = 0;
		
		row = sheet.createRow(rowNumber++);
		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue("Project");

		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue("Project code");

		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue("Customer");

		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue("Employee");

		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue("Hours");

		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue("Rate");

		cell = row.createCell(cellNumber++);
		cell.setCellStyle(headerCellStyle);
		cell.setCellValue("Turnover");
		
		return rowNumber;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.web.report.excel.BaseExcelReportAction#getExcelReportName()
	 */
	@Override
	protected String getExcelReportName()
	{
		return "ProjectReport";
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.web.report.excel.BaseExcelReportAction#getHeaderReportName()
	 */
	@Override
	protected String getHeaderReportName()
	{
		return "Project report";
	}

}
