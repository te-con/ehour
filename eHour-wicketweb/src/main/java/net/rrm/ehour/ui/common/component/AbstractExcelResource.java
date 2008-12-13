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

package net.rrm.ehour.ui.common.component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.time.Time;
import org.apache.wicket.util.value.ValueMap;

/**
 * Abstract excel resource which sets content type and rest
 **/

public abstract class AbstractExcelResource extends DynamicWebResource
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9078717513448771202L;
	private final static Logger logger = Logger.getLogger(AbstractExcelResource.class);
	
	private final String	FONT_TYPE = "Arial";
	private HSSFFont		boldFont;
	private HSSFFont		normalFont;
	protected HSSFCellStyle	boldCellStyle;
	protected HSSFCellStyle	headerCellStyle;
	protected HSSFCellStyle	valueDigitCellStyle;
	protected HSSFCellStyle	defaultCellStyle;
	protected HSSFCellStyle	currencyCellStyle;
	protected HSSFCellStyle	dateBoldCellStyle;
	protected HSSFCellStyle	dateCellStyle;	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.DynamicWebResource#getResourceState()
	 */
	@Override
	protected ResourceState getResourceState()
	{
		ValueMap 		params = getParameters();
		ExcelResourceState	state = new ExcelResourceState();
		
		if (params.containsKey("reportId"))
		{
			String reportId = params.getString("reportId");
			
			try
			{
				byte[] data = getExcelData(reportId);
				state.setData(data);
				
			} catch (Exception e)
			{
				// FIXME handle better
				e.printStackTrace();
				logger.error("While creating excel report", e);
			}
		}
		else
		{
			logger.error("No reportId parameter provided");
		}
		
		return state;
	}
	
	/**
	 * Get excel data as a byte array
	 * @return
	 */
	public abstract byte[] getExcelData(String reportId) throws Exception;
	
	protected abstract String getFilename();
	
	/**
	 * Helper metod for poi workbook -> byte array
	 * @return
	 */
	protected byte[] workbookToByteArray(HSSFWorkbook workbook)
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		try
		{
			workbook.write(output);
		} catch (IOException e)
		{
			logger.error("Failed to generate excel report", e);
			e.printStackTrace();
		}
		
		return output.toByteArray();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.DynamicWebResource#setHeaders(org.apache.wicket.protocol.http.WebResponse)
	 */
	protected void setHeaders(WebResponse response)
	{
//		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.setAttachmentHeader(getFilename());
	}
	
	/**
	 * Initialize cellstyles
	 * @param workbook
	 * @return
	 */
	protected void initCellStyles(HSSFWorkbook workbook)
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
		
		dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setFont(normalFont);
		dateCellStyle.setDataFormat((short)0xf);		
	}	
	
	/**
	 * Resource state
	 * @author Thies
	 *
	 */
	private class ExcelResourceState extends ResourceState
	{
		private byte[] data;
		
		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.markup.html.DynamicWebResource$ResourceState#getContentType()
		 */
		@Override
		public String getContentType()
		{
			return "application/x-ms-excel";
		}

		@Override
		public byte[] getData()
		{
			return data;
		}

		void setData(byte[] data)
		{
			this.data = data;
		}

		@Override
		public int getLength()
		{
			return data.length;
		}

		@Override
		public Time lastModifiedTime()
		{
			return Time.now();
		}
	}
}
