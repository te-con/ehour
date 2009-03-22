/**
 * Created on Sep 15, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.common.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.rrm.ehour.ui.common.component.AbstractExcelResource;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.report.TreeReportElement;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Abstract aggregate excel report
 **/
@SuppressWarnings("deprecation")
public abstract class AbstractExcelReport extends AbstractExcelResource
{
	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger.getLogger(AbstractExcelReport.class);
	
	private	byte[]			excelData;
	private ReportConfig	reportConfig;
	
	/**
	 * 
	 * @param reportConfig
	 */
	public AbstractExcelReport(ReportConfig reportConfig)
	{
		this.reportConfig = reportConfig;
	}	
	
	/**
	 * Get the excel data, cache once created
	 * @throws Exception 
	 */
	@Override
	public byte[] getExcelData(String reportId) throws Exception
	{
		EhourWebSession session = (EhourWebSession)Session.get();
		Report report = (Report)session.getReportCache().getObjectFromCache(reportId);
		
		logger.trace("Creating excel report");
		HSSFWorkbook workbook = createWorkbook(report);
		excelData = workbookToByteArray(workbook);
		
		return excelData;
	}
	
	/**
	 * Create the workbook
	 * @param treeReport
	 * @return
	 */
	private HSSFWorkbook createWorkbook(Report treeReport)
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet 	sheet = wb.createSheet((String)getExcelReportName().getObject());
		int			rowNumber = 0;
		short		column;
		
		for (column = 0; column < 4; column++)
		{
			sheet.setColumnWidth(column, 5000);
		}

		for (; column < 7; column++)
		{
			sheet.setColumnWidth(column, 3000);
		}

		initCellStyles(wb);
		
		rowNumber = createHeaders(rowNumber, sheet, treeReport);
		
		rowNumber = addColumnHeaders(rowNumber, sheet);
		
		fillReportSheet(treeReport, sheet, rowNumber);
		
		return wb;		
	}
	
	/**
	 * Get report name for the filename
	 * @return
	 */
	protected abstract IModel getExcelReportName();
	
	/**
	 * Get report header
	 * @return
	 */
	protected abstract IModel getHeaderReportName();

	
	/**
	 * Add column headers
	 * @param rowNumber
	 * @param sheet
	 * @return
	 */
	private int addColumnHeaders(int rowNumber, HSSFSheet sheet)
	{
		HSSFRow		row;
		HSSFCell	cell;
		short		cellNumber = 0;
		IModel		headerModel;
		
		row = sheet.createRow(rowNumber++);
		
		for (ReportColumn reportColumn : reportConfig.getReportColumns())
		{
			if (reportColumn.isVisible())
			{
				headerModel = new ResourceModel(reportColumn.getColumnHeaderResourceKey());
				
				cell = row.createCell(cellNumber++);
				cell.setCellStyle(headerCellStyle);
				
				cell.setCellValue(new HSSFRichTextString((String)headerModel.getObject()));
			}
		} 

		return rowNumber;
	}
	
	
	/**
	 * Fill report sheet
	 * @param reportData
	 * @param sheet
	 * @param rowNumber
	 */
	@SuppressWarnings("unchecked")
	protected void fillReportSheet(Report reportData, HSSFSheet sheet, int rowNumber)
	{
		List<TreeReportElement> matrix = (List<TreeReportElement>)reportData.getReportData().getReportElements();
		ReportColumn[]	columnHeaders = reportConfig.getReportColumns();
		HSSFRow				row;
		HSSFCell			cell;
		
		for (TreeReportElement element : matrix)
		{
			Serializable[] rowValues = element.getRow();
			
			int 	i = 0;
			short	cellNumber = 0;
			
			row = sheet.createRow(rowNumber++);
			
			// add cells for a row
			for (Serializable cellValue : rowValues)
			{
				if (columnHeaders[i].isVisible())
				{
					cell = row.createCell(cellNumber++);

					if (cellValue != null)
					{
						if (columnHeaders[i].getColumnType() == ReportColumn.ColumnType.HOUR)
						{
							cell.setCellStyle(valueDigitCellStyle);
							
							if (cellValue instanceof Float)
							{
								cell.setCellValue((Float)cellValue);
							}
							else
							{
								cell.setCellValue(((Number)cellValue).doubleValue());
							}
						}
						else if (columnHeaders[i].getColumnType() == ReportColumn.ColumnType.TURNOVER
								 || columnHeaders[i].getColumnType() == ReportColumn.ColumnType.RATE)
						{
							cell.setCellStyle(currencyCellStyle);
							
							if (cellValue instanceof Float)
							{
								cell.setCellValue((Float)cellValue);
							}
							else 
							{
								cell.setCellValue( ((Number)cellValue).doubleValue());
							}
						}
						else if (columnHeaders[i].getColumnType() == ReportColumn.ColumnType.DATE)
						{
							cell.setCellStyle(dateCellStyle);
							cell.setCellValue((Date)cellValue);
						}
						else
						{
							cell.setCellStyle(defaultCellStyle);
							cell.setCellValue(new HSSFRichTextString((String)cellValue));
						}
					}
				}
				
				i++;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.component.AbstractExcelReport#getFilename()
	 */
	@Override
	protected String getFilename()
	{
		return ((String)(getExcelReportName().getObject())).toLowerCase().replace(' ', '_') + ".xls";
	}
	
	
	/**
	 * Create header containing report date
	 * @param sheet
	 */
	protected int createHeaders(int rowNumber, HSSFSheet sheet, Report report)
	{
		HSSFRow		row;
		HSSFCell	cell;

		row = sheet.createRow(rowNumber++);
		cell = row.createCell(0);
		cell.setCellStyle(boldCellStyle);
		cell.setCellValue(new HSSFRichTextString(CommonWebUtil.getResourceModelString(getHeaderReportName())));
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)1));

		row = sheet.createRow(rowNumber++);
		cell = row.createCell(0);
		cell.setCellStyle(boldCellStyle);
		cell.setCellValue(new HSSFRichTextString(CommonWebUtil.getResourceModelString(new ResourceModel("report.dateStart"))));
		
		cell = row.createCell(1);
		
		if (report.getReportRange() == null || 
				report.getReportRange().getDateStart() == null)
		{
			cell.setCellStyle(boldCellStyle);
			cell.setCellValue("--");
		}
		else
		{
			cell.setCellStyle(dateBoldCellStyle);
			cell.setCellValue(report.getReportCriteria().getReportRange().getDateStart());
		}

		cell = row.createCell(3);
		cell.setCellStyle(boldCellStyle);
		cell.setCellValue(new HSSFRichTextString(CommonWebUtil.getResourceModelString(new ResourceModel("report.dateEnd"))));
		
		cell = row.createCell(4);
		if (report.getReportRange() == null || report.getReportRange().getDateEnd() == null)
		{
			cell.setCellStyle(boldCellStyle);
			cell.setCellValue("--");
		}
		else
		{
			cell.setCellStyle(dateBoldCellStyle);
			cell.setCellValue(report.getReportRange().getDateEndForDisplay());
		}
		
		rowNumber++;
		
		return rowNumber;
	}	
}
