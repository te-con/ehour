/**
 * Created on Jun 3, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.model;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Date model 
 **/

public class DateModel implements IModel
{
	public final static int	DATESTYLE_LONG = 1;
	public final static int	DATESTYLE_MONTHONLY = 2;
	public final static int DATESTYLE_TIMESHEET_DAYLONG = 3;
	public final static int DATESTYLE_TIMESHEET_DAYONLY = 4;
	public final static int	DATESTYLE_DAYONLY = 5;
	public final static int	DATESTYLE_FULL_SHORT = 6;
	public final static int	DATESTYLE_WEEK= 7;
	
	private static final long serialVersionUID = 431440606497572025L;
	private IModel					model;
	private	final DateFormat	dateFormatter;
	
	/**
	 * 
	 * @param calendar
	 * @param config
	 */
	public DateModel(Calendar calendar, EhourConfig config)
	{
		this(calendar.getTime(), config, DATESTYLE_LONG);
	}

	/**
	 * 
	 * @param date
	 * @param config
	 * @param dateStyle
	 */
	public DateModel(Date date, EhourConfig config)
	{
		this(date, config, DATESTYLE_LONG);
	}
	
	
	/**
	 * 
	 * @param date
	 * @param config
	 * @param dateStyle
	 */
	public DateModel(Date date, EhourConfig config, int dateStyle)
	{
		this(new Model(date), config, dateStyle);
	}
	
	/**
	 * 
	 * @param calendar
	 * @param config
	 * @param dateStyle
	 */
	public DateModel(Calendar calendar, EhourConfig config, int dateStyle)
	{
		this(calendar.getTime(), config, dateStyle);
	}

	/**
	 * 
	 * @param model
	 * @param config
	 * @param dateStyle
	 */
	public DateModel(IModel model, EhourConfig config, int dateStyle)
	{
		this(model, config.getLocale(), dateStyle);
		
	}
	
	/**
	 * 
	 * @param model
	 * @param locale
	 * @param dateStyle
	 */
	public DateModel(IModel model, Locale locale, int dateStyle)
	{
		this.model = model;
		
		switch (dateStyle)
		{
			case DATESTYLE_MONTHONLY:
				dateFormatter = new SimpleDateFormat("MMMM yyyy", locale);
				break;
			case DATESTYLE_TIMESHEET_DAYLONG:
				dateFormatter = new TimesheetLongFormatter("EEE d", locale);
				break;
			case DATESTYLE_TIMESHEET_DAYONLY:
				dateFormatter = new TimesheetLongFormatter("EEE", locale);
				break;
			case DATESTYLE_DAYONLY:
				dateFormatter = new TimesheetLongFormatter("dd", locale);
				break;
			case DATESTYLE_FULL_SHORT:
				dateFormatter = new TimesheetLongFormatter(DateUtil.getPatternForDateLocale(locale), locale);
				break;
			case DATESTYLE_WEEK:
				dateFormatter = new TimesheetLongFormatter("w", locale);
				break;
			case DATESTYLE_LONG:
			default:
				dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
				break;
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	public Object getObject()
	{
		return (model == null || model.getObject() == null) ? "&infin;" : dateFormatter.format((Date)model.getObject());
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.Model#setObject(java.lang.Object)
	 */
	public void setObject(Object value)
	{
		this.model = (IModel)value;
	}
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private class TimesheetLongFormatter extends SimpleDateFormat
	{
		private static final long serialVersionUID = 2697598002926018462L;

		public TimesheetLongFormatter(String format, Locale locale)
		{
			super(format, locale);
		}
		
		@Override
	    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition)
		{
			StringBuffer sb = super.format(date, toAppendTo, fieldPosition);
			
			String formatted = sb.toString();
			
			return new StringBuffer(formatted.replace(" ", "<br />"));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	public void detach()
	{
	}
}
