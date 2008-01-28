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

package net.rrm.ehour.ui.component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.time.Time;
import org.apache.wicket.util.value.ValueMap;

/**
 * Abstract excel resource which sets content type and rest
 **/

public abstract class AbstractExcelResource extends DynamicWebResource
{
	private final static Logger logger = Logger.getLogger(AbstractExcelResource.class);
	
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
