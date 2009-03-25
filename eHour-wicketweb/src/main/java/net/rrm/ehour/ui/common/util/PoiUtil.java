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

package net.rrm.ehour.ui.common.util;

import net.rrm.ehour.ui.common.report.ExcelWorkbook;
import net.rrm.ehour.ui.common.report.ExcelWorkbook.StyleType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * Created on Mar 25, 2009, 6:45:57 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class PoiUtil
{
	public static void createCell(HSSFRow row, int column, String value, ExcelWorkbook workbook)
	{
		PoiUtil.createCell(row, column, value, StyleType.DEFAULT, workbook);
	}

	public static void createCell(HSSFRow row, int column, String value, StyleType cellStyle, ExcelWorkbook workbook)
	{
		HSSFCell projectCell = row.createCell(column);
		projectCell.setCellStyle(workbook.getCellStyle(cellStyle));
		projectCell.setCellValue(new HSSFRichTextString(value));
	}

	public static void createCell(HSSFRow row, int column, double value, StyleType cellStyle, ExcelWorkbook workbook)
	{
		HSSFCell projectCell = row.createCell(column);
		projectCell.setCellStyle(workbook.getCellStyle(cellStyle));
		projectCell.setCellValue(value);
	}	
}
