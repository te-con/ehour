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

/**
 * Abstract excel report which sets content type and rest
 **/

public abstract class AbstractExcelReport extends DynamicWebResource
{
	private final static Logger logger = Logger.getLogger(AbstractExcelReport.class);
	
	/**
	 * Filename of the downloadable resource
	 * @param filename
	 */
	public AbstractExcelReport(String filename)
	{
		super(filename);
	}

	/**
	 * Get excel data as a byte array
	 * @return
	 */
	protected abstract byte[] getExcelData();
	
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
	 * @see org.apache.wicket.markup.html.DynamicWebResource#getResourceState()
	 */
	@Override
	protected ResourceState getResourceState()
	{
		return new ExcelResourceState();
	}

	/**
	 * Resource state
	 * @author Thies
	 *
	 */
	private class ExcelResourceState extends ResourceState
	{

		@Override
		public String getContentType()
		{
			return "application/x-ms-excel";
		}

		@Override
		public byte[] getData()
		{
			return getExcelData();
		}
	}

}
