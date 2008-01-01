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

package net.rrm.ehour.ui.panel.report;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.ui.component.AbstractExcelReport;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Abstract aggregate excel report
 **/

public abstract class AbstractAggregateExcelReport extends AbstractExcelReport
{
	private final static Logger logger = Logger.getLogger(AbstractAggregateExcelReport.class);
	
	private final String	FONT_TYPE = "Arial";
	private HSSFFont		boldFont;
	private HSSFFont		normalFont;
	protected HSSFCellStyle	boldCellStyle;
	protected HSSFCellStyle	headerCellStyle;
	protected HSSFCellStyle	valueDigitCellStyle;
	protected HSSFCellStyle	defaultCellStyle;
	protected HSSFCellStyle	currencyCellStyle;
	protected HSSFCellStyle	dateBoldCellStyle;	
	private	byte[]			excelData;
	private ReportConfig	reportConfig;
	
	/**
	 * 
	 * @param reportConfig
	 */
	public AbstractAggregateExcelReport(ReportConfig reportConfig)
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
		TreeReport report = (TreeReport)session.getReportCache().getReportFromCache(reportId);
		
		if (report == null)
		{
			throw new Exception("No report found in cache");
		}
		
		logger.info("Creating excel report");
		HSSFWorkbook workbook = createWorkbook(report);
		excelData = workbookToByteArray(workbook);
		
		return excelData;
	}
	
	/**
	 * Create the workbook
	 * @param treeReport
	 * @return
	 */
	private HSSFWorkbook createWorkbook(TreeReport treeReport)
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet 	sheet = wb.createSheet((String)getExcelReportName().getObject());
		int			rowNumber = 0;
		short		column;
		
		for (column = 0; column < 4; column++)
		{
			sheet.setColumnWidth(column, (short)5000);
		}

		for (; column < 7; column++)
		{
			sheet.setColumnWidth(column, (short)3000);
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
		
		for (TreeReportColumn treeReportColumn : reportConfig.getReportColumns())
		{
			if (treeReportColumn.isVisible())
			{
				headerModel = new ResourceModel(treeReportColumn.getColumnHeaderResourceKey());
				
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
	protected void fillReportSheet(TreeReport reportData, HSSFSheet sheet, int rowNumber)
	{
		List<ReportNode> nodes = reportData.getNodes();
		
		for (ReportNode reportNode : nodes)
		{
			rowNumber = addNodeToSheet(reportNode, sheet, rowNumber);
		}
	}	
	
	/**
	 * 
	 * @param node
	 * @param sheet
	 * @param rowNumber
	 * @return
	 */
	private int addNodeToSheet(ReportNode reportNode, HSSFSheet sheet, int rowNumber)
	{
		Serializable[][]	matrix = reportNode.getNodeMatrix(reportConfig.getReportColumns().length);
		TreeReportColumn[]	columnHeaders = reportConfig.getReportColumns();
		HSSFRow				row;
		HSSFCell			cell;
		
		for (Serializable[] rowValues : matrix)
		{
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
						if (columnHeaders[i].getColumnType() == TreeReportColumn.ColumnType.HOUR)
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
						else if (columnHeaders[i].getColumnType() == TreeReportColumn.ColumnType.TURNOVER
								 || columnHeaders[i].getColumnType() == TreeReportColumn.ColumnType.RATE)
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
		
		return rowNumber;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.component.AbstractExcelReport#getFilename()
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
	private int createHeaders(int rowNumber, HSSFSheet sheet, TreeReport report)
	{
		HSSFRow		row;
		HSSFCell	cell;

		row = sheet.createRow(rowNumber++);
		cell = row.createCell((short)0);
		cell.setCellStyle(boldCellStyle);
		cell.setCellValue(new HSSFRichTextString((String)getHeaderReportName().getObject()));
		sheet.addMergedRegion(new Region(0, (short)0, 0, (short)1));

		row = sheet.createRow(rowNumber++);
		cell = row.createCell((short)0);
		cell.setCellStyle(boldCellStyle);
		cell.setCellValue(new HSSFRichTextString("Start date:"));

		cell = row.createCell((short)1);
		cell.setCellStyle(dateBoldCellStyle);
		cell.setCellValue(report.getReportRange().getDateStart());

		cell = row.createCell((short)3);
		cell.setCellStyle(boldCellStyle);
		cell.setCellValue(new HSSFRichTextString("End date:"));

		
		cell = row.createCell((short)4);
		cell.setCellStyle(dateBoldCellStyle);
		cell.setCellValue(report.getReportRange().getDateEndForDisplay());
		
		rowNumber++;
		
		return rowNumber;
	}	
	
	/**
	 * Initialize cellstyles
	 * @param workbook
	 * @return
	 */
	private void initCellStyles(HSSFWorkbook workbook)
	{
		HSSFPalette palette = workbook.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 231, (byte) 243, (byte) 255);
		
		headerCellStyle = workbook.createCellStyle();
		
		boldFont = workbook.createFont();
		boldFont.setFontName(FONT_TYPE);
		boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerCellStyle.setFont(boldFont);
		headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		headerCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		headerCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

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
}
