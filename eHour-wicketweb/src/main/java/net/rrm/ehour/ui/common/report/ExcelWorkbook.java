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

package net.rrm.ehour.ui.common.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Extension of standard POI workbook which adds default formatting 
 * Created on Mar 23, 2009, 7:14:35 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExcelWorkbook extends HSSFWorkbook
{
	private final String FONT_TYPE = "Arial";
	
	public enum StyleType
	{
		HEADER,
		BOLD,
		VALUE_DIGIT,
		NORMAL,
		CURRENCY,
		DATE_BOLD,
		DATE_NORMAL
	};
	
	private HSSFFont		boldFont;
	private HSSFFont		normalFont;
	
	private Map<StyleType, HSSFCellStyle> cellStyles;
	
	public ExcelWorkbook()
	{
		initializeCellStyles();
	}
	
	public HSSFCellStyle getCellStyle(StyleType type)
	{
		return cellStyles.get(type);
	}
	
	public byte[] toByteArray() throws IOException
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		this.write(output);
		
		return output.toByteArray();
	}
	
	private void initializeCellStyles()
	{
		cellStyles = new HashMap<StyleType, HSSFCellStyle>();
		
		createFonts();

		HSSFCellStyle headerCellStyle = createCellStyle();
		headerCellStyle.setFont(boldFont);
		headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		headerCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		headerCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyles.put(StyleType.HEADER, headerCellStyle);
		
		HSSFCellStyle boldCellStyle = createCellStyle();
		boldCellStyle.setFont(boldFont);
		cellStyles.put(StyleType.BOLD, boldCellStyle);

		HSSFCellStyle dateBoldCellStyle = createCellStyle();
		dateBoldCellStyle.setFont(boldFont);
		dateBoldCellStyle.setDataFormat((short)0xf);
		cellStyles.put(StyleType.DATE_BOLD, dateBoldCellStyle);
		
		HSSFCellStyle defaultCellStyle = createCellStyle();
		defaultCellStyle.setFont(normalFont);
		cellStyles.put(StyleType.NORMAL, defaultCellStyle);
		
		HSSFCellStyle valueDigitCellStyle = createCellStyle();
		valueDigitCellStyle.setFont(normalFont);
		valueDigitCellStyle.setDataFormat((short)2);
		cellStyles.put(StyleType.VALUE_DIGIT, valueDigitCellStyle);

		HSSFCellStyle currencyCellStyle= createCellStyle();
		currencyCellStyle.setFont(normalFont);
		currencyCellStyle.setDataFormat((short)0x7);
		cellStyles.put(StyleType.CURRENCY, currencyCellStyle);
		
		HSSFCellStyle dateCellStyle = createCellStyle();
		dateCellStyle.setFont(normalFont);
		dateCellStyle.setDataFormat((short)0xf);		
		cellStyles.put(StyleType.DATE_NORMAL, dateCellStyle);
	}

	private void createFonts()
	{
		boldFont = createFont();
		boldFont.setFontName(FONT_TYPE);
		boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		normalFont = createFont();
		normalFont.setFontName(FONT_TYPE);
	}
}
