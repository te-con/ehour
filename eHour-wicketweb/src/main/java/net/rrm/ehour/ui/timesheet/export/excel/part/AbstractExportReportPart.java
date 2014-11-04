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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.ExcelStyle;
import net.rrm.ehour.ui.common.report.excel.ExcelWorkbook;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created on Mar 25, 2009, 3:34:34 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public abstract class AbstractExportReportPart
{
	private int cellMargin;
	private EhourConfig config;
	private SimpleDateFormat formatter;
	private Sheet sheet;
	private Report report;
	private ExcelWorkbook workbook;
	
	public AbstractExportReportPart(int cellMargin, Sheet sheet, Report report, ExcelWorkbook workbook)
	{
		this.cellMargin = cellMargin;
		this.sheet = sheet;
		this.report = report;
		this.workbook = workbook;
		
		init();
	}
	
	public abstract int createPart(int rowNumber);
	
	private void init()
	{
		config = EhourWebSession.getEhourConfig();
		Locale locale = config.getFormattingLocale();
		formatter = new SimpleDateFormat("dd MMM yy", locale);
	}

	protected int getCellMargin()
	{
		return cellMargin;
	}

	protected EhourConfig getConfig()
	{
		return config;
	}

	protected SimpleDateFormat getFormatter()
	{
		return formatter;
	}

	protected Sheet getSheet()
	{
		return sheet;
	}

	protected Report getReport()
	{
		return report;
	}

	protected ExcelWorkbook getWorkbook()
	{
		return workbook;
	}
	
	protected void createEmptyCells(Row row, ExcelStyle excelStyle)
	{
		for (int i : ExportReportColumn.EMPTY.getColumns())
		{
			CellFactory.createCell(row, getCellMargin() + i, getWorkbook(), excelStyle);
		}
	}	
}
