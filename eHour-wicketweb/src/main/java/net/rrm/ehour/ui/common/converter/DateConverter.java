package net.rrm.ehour.ui.common.converter;

import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateConverter implements IConverter
{
	private static final long serialVersionUID = -5004560809428503944L;
	
	public enum DateStyle
	{
		LONG,
		MONTH_ONLY,
		TIMESHEET_DAY_LONG,
		TIMESHEET_DAY_ONLY,
		DAY_ONLY,
		FULL_SHORT,
		WEEK,
		DAY_ONLY_LONG,
		DATE_TIME
	}
	
	private DateStyle dateStyle;
	private String nullString;
	
	public DateConverter(DateStyle dateStyle, String nullString)
	{
		this.dateStyle = dateStyle;
		this.nullString = nullString;
	}

    /**
	 * Init formatter and fetch locale from session's config
	 * @param dateStyle
	 * @return 
	 */
	private DateFormat initFormatter(DateStyle dateStyle)
	{
		return initFormatter(EhourWebSession.getSession().getEhourConfig().getLocale(), dateStyle);
	}
	
	/**
	 * Init simple date formatter
	 * @param locale
	 * @param dateStyle
	 * @return 
	 */
	private DateFormat initFormatter(Locale locale, DateStyle dateStyle)
	{
		DateFormat dateFormatter;
		
		switch (dateStyle)
		{
			case MONTH_ONLY:
				dateFormatter = new SimpleDateFormat("MMMM yyyy", locale);
				break;
			case TIMESHEET_DAY_LONG:
				dateFormatter = new TimesheetLongFormatter("EEE d", locale);
				break;
			case TIMESHEET_DAY_ONLY:
				dateFormatter = new TimesheetLongFormatter("EEE", locale);
				break;
			case DAY_ONLY:
				dateFormatter = new TimesheetLongFormatter("dd", locale);
				break;
			case DAY_ONLY_LONG:
				dateFormatter = new TimesheetLongFormatter("EEEE", locale);
				break;
			case FULL_SHORT:
				dateFormatter = new TimesheetLongFormatter(DateUtil.getPatternForDateLocale(locale), locale);
				break;
			case WEEK:
				dateFormatter = new TimesheetLongFormatter("w", locale);
				break;
			case DATE_TIME:
				dateFormatter = new TimesheetLongFormatter("dd MMM yy HH:mm:ss", locale, false);
				break;
			case LONG:
			default:
				dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
				break;
		}		
		
		return dateFormatter;
	}
	
	public Object convertToObject(String value, Locale locale)
	{
        DateFormat dateFormatter = initFormatter(dateStyle);
		
		try
		{
			return dateFormatter.parseObject(value);
		} catch (ParseException e)
		{
			return null;
		}
	}

	public String convertToString(Object value, Locale locale)
	{
		DateFormat dateFormatter = initFormatter(dateStyle);

		Date date = null;
		
		if (value instanceof Calendar)
		{
			Calendar cal = (Calendar)value;
			date = cal.getTime();
		}
		else if (value instanceof Date)
		{
			date = (Date)value;
		}
		else if (value instanceof IModel<?>)
		{
			date = (Date) ((IModel<?>)value).getObject();
		}
		
		return date == null ? nullString : dateFormatter.format(date);
	}

	
	private static class TimesheetLongFormatter extends SimpleDateFormat
	{
		private static final long serialVersionUID = 2697598002926018462L;
		private boolean breakSpaces = true;
		
		public TimesheetLongFormatter(String format, Locale locale)
		{
			this(format, locale, true);
		}

		public TimesheetLongFormatter(String format, Locale locale, boolean breakSpaces)
		{
			super(format, locale);
			this.breakSpaces =breakSpaces;
		}

		@Override
	    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition)
		{
			StringBuffer sb = super.format(date, toAppendTo, fieldPosition);
			
			if (breakSpaces)
			{
				return new StringBuffer(sb.toString().replace(" ", "<br />"));
			}
			else
			{
				return sb;
			}
		}
	}
}
