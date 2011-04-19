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

package net.rrm.ehour.ui.common.report.excel;

import net.rrm.ehour.ui.common.util.CommonWebUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Created on Mar 25, 2009, 6:45:57 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class CellFactory
{
	public static HSSFCell createCell(HSSFRow row, int column, String value, HSSFWorkbook workbook)
	{
		return CellFactory.createCell(row, column, value, workbook, CellStyle.NORMAL);
	}

	public static HSSFCell createCell(HSSFRow row, int column, ResourceModel valueModel, HSSFWorkbook workbook)
	{
		return CellFactory.createCell(row, column, valueModel, workbook, CellStyle.NORMAL);
	}

	public static HSSFCell createCell(HSSFRow row, int column, HSSFWorkbook workbook, CellStyle... cellStyles)
	{
		return createCell(row, column, "", workbook, cellStyles);
	}
	
	public static HSSFCell createCell(HSSFRow row, int column, IModel<String> valueModel, HSSFWorkbook workbook, CellStyle... cellStyles)
	{
		return createCell(row, column, CommonWebUtil.getResourceModelString(valueModel), workbook, cellStyles);
	}
	
	public static HSSFCell createCell(HSSFRow row, int column, Object value, HSSFWorkbook workbook, CellStyle... cellStyles)
	{
		HSSFCell cell = row.createCell(column);
		
		if (value instanceof Float)
		{
			cell.setCellValue((Float)value);
		}
		else if (value instanceof Number)
		{
			cell.setCellValue( ((Number)value).doubleValue());
		}
		else
		{
			cell.setCellValue(new HSSFRichTextString(value.toString()));
		}
		
		cell.setCellStyle(applyCellStyles(workbook, cellStyles));
		
		return cell;
	}
	
	private static HSSFCellStyle applyCellStyles(HSSFWorkbook workbook, CellStyle... cellStyles)
	{
		HSSFCellStyle style = workbook.createCellStyle();
		
		for (CellStyle cellStyle : cellStyles)
		{
			cellStyle.getCellStyleElement().populate(style, workbook);
		}
		
		if (style.getFont(workbook) == null)
		{
			new CellStyleImpl.NormalFont().populate(style, workbook);
		}

		return style;
	}
}
