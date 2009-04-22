/**
 * Created on Apr 17, 2009
 * Author: Thies
 *
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

package net.rrm.ehour.ui.common.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class PoiUtil
{
	public static byte[] getWorkbookAsBytes(HSSFWorkbook workbook) throws IOException
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		workbook.write(output);
		
		byte[] excelData = output.toByteArray();
		
		return excelData;
	}
	
	public static int getImageType(String type)
	{
		if (type.equalsIgnoreCase("png"))
		{
			return HSSFWorkbook.PICTURE_TYPE_PNG;
		}
		else
		{
			return HSSFWorkbook.PICTURE_TYPE_JPEG;
		}
	}
}
