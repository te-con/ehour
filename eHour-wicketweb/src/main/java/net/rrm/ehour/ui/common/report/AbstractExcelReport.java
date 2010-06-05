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

package net.rrm.ehour.ui.common.report;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.ui.common.component.AbstractExcelResource;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.CellStyle;
import net.rrm.ehour.ui.report.TreeReportElement;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Abstract aggregate excel report
 **/
public abstract class AbstractExcelReport extends AbstractExcelResource
{
	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger.getLogger(AbstractExcelReport.class);
	
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
	 * @throws IOException 
	 * @throws Exception 
	 */
	@Override
	public byte[] getExcelData(Report report) throws IOException
	{
		logger.trace("Creating excel report");
		HSSFWorkbook workbook = createWorkbook(report);

		return PoiUtil.getWorkbookAsBytes(workbook);
	}
	
	/**
	 * Create the workbook
	 * @param treeReport
	 * @return
	 */
	protected HSSFWorkbook createWorkbook(Report treeReport)
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

		rowNumber = createHeaders(rowNumber, sheet, treeReport, wb);
		
		rowNumber = addColumnHeaders(rowNumber, sheet, wb);
		
		fillReportSheet(treeReport, sheet, rowNumber, wb);
		
		return wb;		
	}
	
	/**
	 * Get report name for the filename
	 * @return
	 */
	protected abstract IModel<String> getExcelReportName();
	
	/**
	 * Get report header
	 * @return
	 */
	protected abstract IModel<String> getHeaderReportName();

	
	/**
	 * Add column headers
	 * @param rowNumber
	 * @param sheet
	 * @return
	 */
	private int addColumnHeaders(int rowNumber, HSSFSheet sheet, HSSFWorkbook workbook)
	{
		HSSFRow		row;
		int			cellNumber = 0;
		IModel<String> headerModel;
		
		row = sheet.createRow(rowNumber++);
		
		for (ReportColumn reportColumn : reportConfig.getReportColumns())
		{
			if (reportColumn.isVisible())
			{
				headerModel = new ResourceModel(reportColumn.getColumnHeaderResourceKey());
				
				CellFactory.createCell(row, cellNumber++, headerModel, workbook, CellStyle.HEADER);
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
	protected void fillReportSheet(Report reportData, HSSFSheet sheet, int rowNumber, HSSFWorkbook workbook)
	{
		List<TreeReportElement> matrix = (List<TreeReportElement>)reportData.getReportData().getReportElements();
		ReportColumn[]	columnHeaders = reportConfig.getReportColumns();
		HSSFRow				row;
		
		for (TreeReportElement element : matrix)
		{
			row = sheet.createRow(rowNumber++);

			addColumns(workbook, columnHeaders, row, element);
		}
	}

	private void addColumns(HSSFWorkbook workbook, ReportColumn[] columnHeaders, HSSFRow row, TreeReportElement element)
	{
		int	i = 0;
		int cellNumber = 0;
		
		
		// add cells for a row
		for (Serializable cellValue : element.getRow())
		{
			if (columnHeaders[i].isVisible())
			{
				if (cellValue != null)
				{
					if (columnHeaders[i].getColumnType() == ReportColumn.ColumnType.HOUR)
					{
						CellFactory.createCell(row, cellNumber++, cellValue, workbook, CellStyle.DIGIT);
					}
					else if (columnHeaders[i].getColumnType() == ReportColumn.ColumnType.TURNOVER
							 || columnHeaders[i].getColumnType() == ReportColumn.ColumnType.RATE)
					{
						CellFactory.createCell(row, cellNumber++, cellValue, workbook, CellStyle.CURRENCY);
					}
					else if (columnHeaders[i].getColumnType() == ReportColumn.ColumnType.DATE)
					{
						CellFactory.createCell(row, cellNumber++, cellValue, workbook, CellStyle.DATE);
					}
					else
					{
						CellFactory.createCell(row, cellNumber++, cellValue, workbook, CellStyle.NORMAL);
					}
				}
				else
				{
					cellNumber++;
				}
			}
			
			i++;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.common.component.AbstractExcelReport#getFilename()
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
	protected int createHeaders(int rowNumber, HSSFSheet sheet, Report report, HSSFWorkbook workbook)
	{
		HSSFRow		row;

		row = sheet.createRow(rowNumber++);
		CellFactory.createCell(row, 0, getHeaderReportName(), workbook, CellStyle.BOLD);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

		row = sheet.createRow(rowNumber++);
		CellFactory.createCell(row, 0, new ResourceModel("report.dateStart"), workbook, CellStyle.BOLD);
		
		if (report.getReportRange() == null || 
				report.getReportRange().getDateStart() == null)
		{
			CellFactory.createCell(row, 1, "--", workbook, CellStyle.BOLD);
		}
		else
		{
			CellFactory.createCell(row, 1, report.getReportCriteria().getReportRange().getDateStart(), workbook, CellStyle.BOLD, CellStyle.DATE);
		}

		CellFactory.createCell(row, 3, new ResourceModel("report.dateEnd"), workbook, CellStyle.BOLD);
		
		if (report.getReportRange() == null || report.getReportRange().getDateEnd() == null)
		{
			CellFactory.createCell(row, 4, "--", workbook, CellStyle.BOLD);
		}
		else
		{
			CellFactory.createCell(row, 4, report.getReportCriteria().getReportRange().getDateEnd(), workbook, CellStyle.BOLD, CellStyle.DATE);
		}
		
		rowNumber++;
		
		return rowNumber;
	}	
}
