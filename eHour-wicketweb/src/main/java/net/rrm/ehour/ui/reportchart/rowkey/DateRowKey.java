/**
 * Created on Jan 2, 2008
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

package net.rrm.ehour.ui.reportchart.rowkey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Row key for dates 
 **/

public class DateRowKey extends ChartRowKey
{
	private static final DateFormat format = new SimpleDateFormat("yyyyMMdd");
	private Integer id;
	private String name;
	
	public DateRowKey(Date date, Locale locale)
	{
		id = Integer.parseInt(format.format(date));
		
		DateFormat formatter = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
		name = formatter.format(date);
	}

	@Override
	public Integer getId()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

}
