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

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


/**
 * Created on Mar 25, 2009, 4:58:00 PM
 * @author Thies Edeling (thies@te-con.nl)
 *
 */
public class CellStyleElement
{
	private final static String FONT_TYPE = "Arial";

	public static class BoldFont implements CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook)
		{
			HSSFFont font = createFont(workbook);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			cellStyle.setFont(font);
		}
	}

	public static class NormalFont implements CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook)
		{
			HSSFFont font = createFont(workbook);
			cellStyle.setFont(font);
		}
	}

	public static class DateValue implements CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook)
		{
			cellStyle.setDataFormat((short)0xf);
		}
	}

	public static class DigitValue implements CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook)
		{
			cellStyle.setDataFormat((short)2);
		}
	}

	public static class BorderSouth implements CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook)
		{
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		}
	}

	public static class BorderSouthThin implements CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook)
		{
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		}
	}

	public static class BorderThin implements CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook)
		{
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);

			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);

			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);

			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		}
	}

	public static class BorderNorthThin implements CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook)
		{
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		}
	}

	public static class BorderNorth implements CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook)
		{
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		}
	}

	public static class Header implements CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook)
		{
			new BoldFont().populate(cellStyle, workbook);
			new BorderSouth().populate(cellStyle, workbook);

			cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
	}

	private static HSSFFont createFont(HSSFWorkbook workbook)
	{
		HSSFFont font = workbook.createFont();
		font.setFontName(FONT_TYPE);

		return font;
	}

	public interface CellStylePopulator
	{
		public void populate(HSSFCellStyle cellStyle, HSSFWorkbook workbook);
	}
}
