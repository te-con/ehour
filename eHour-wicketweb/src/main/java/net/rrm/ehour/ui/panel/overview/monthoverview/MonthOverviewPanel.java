/**
 * Created on Sep 6, 2007
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

package net.rrm.ehour.ui.panel.overview.monthoverview;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.HtmlUtil;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.ResourceModel;

/**
 * Month overview panel for consultants
 **/

public class MonthOverviewPanel extends Panel
{
	private static final long serialVersionUID = -8977205040520638758L;
	private static final Logger	logger = Logger.getLogger(MonthOverviewPanel.class);
	
	private final TimesheetOverview timesheetOverview;
	private	final int				thisMonth;
	private	final int				thisYear;
	private final Calendar			overviewFor;
	private final EhourConfig		config;
	
	/**
	 * 
	 * @param id
	 */
	public MonthOverviewPanel(String id, TimesheetOverview timesheetOverview, Calendar overviewFor)
	{
		super(id);
		
		this.timesheetOverview = timesheetOverview;
	    thisMonth = overviewFor.get(Calendar.MONTH);
	    thisYear = overviewFor.get(Calendar.YEAR);
	    this.overviewFor = overviewFor;
	    
		setOutputMarkupId(true);
		
		EhourWebSession session = (EhourWebSession)getSession();
		config = session.getEhourConfig();
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("greyFrame", new ResourceModel("monthoverview.overview"));
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		
		greyBorder.add(blueBorder);
		add(greyBorder);
		
		addDayLabels(blueBorder, session.getEhourConfig());
		
		createMonthCalendar(blueBorder, overviewFor);
	}

	/**
	 * Create month calendar
	 * @param parent
	 * @param calendar
	 */
	private void createMonthCalendar(WebMarkupContainer parent, Calendar calendar)
	{
	    logger.debug("Creating month overview calendar for " + calendar.getTime().toString());
	    
	    int currentColumn = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	    calendar.add(Calendar.DATE, -1 * currentColumn);
	    
	    RepeatingView	calendarView = new RepeatingView("calendarView");
	    
	    while ((calendar.get(Calendar.YEAR) == thisYear) &&
	    		(calendar.get(Calendar.MONTH) <= thisMonth) || calendar.get(Calendar.YEAR) < thisYear)
	    {
			logger.debug("Adding month overview row for " + overviewFor.getTime().toString());
			WebMarkupContainer	row = new WebMarkupContainer(calendarView.newChildId());
			calendarView.add(row);
	    	
	    	createWeek(row);
	    }
	    
	    parent.add(calendarView);
	}
	
	/**
	 * 
	 * @param calendar
	 * @param calendarView
	 * @return
	 */
	private void createWeek(WebMarkupContainer row)
	{
		addDayNumbersToRow(row);
		addDayValuesToRow(row);
		overviewFor.add(Calendar.WEEK_OF_YEAR, 1);
	}
	
	/**
	 * 
	 * @param row
	 */
	private void addDayValuesToRow(WebMarkupContainer row)
	{
		List<TimesheetEntry>	timesheetEntries;
		// @todo use a localized version
		DecimalFormat			df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		for (String dayId : DateUtil.daysInWeek)
		{
			dayId += "Value";

			System.out.println(dayId);
			Fragment fragment;
			
	        if (overviewFor.get(Calendar.MONTH) == thisMonth)
	        {
	        	timesheetEntries = timesheetOverview.getTimesheetEntries().get(overviewFor.get(Calendar.DAY_OF_MONTH));
	        	
	            if (timesheetEntries != null
	            		&& timesheetEntries.size() > 0)
	            {
	            	fragment = new Fragment(dayId, "showProjects");
	            	
	            	for (TimesheetEntry entry : timesheetEntries)
					{
	            		if (entry.getHours().intValue() != 0)
	            		{
	            			fragment.add(new Label("projectCode", entry.getEntryId().getProjectAssignment().getProject().getProjectCode())); 
	            			fragment.add(new Label("hours", new FloatModel(entry.getHours(), config)));
	            		}
					}
	            }
	            else
	            {
	            	fragment = new Fragment(dayId, "noProjects");
	            }
	            
	            row.add(fragment);
	        }
	        else
	        {
//            	if (monthIsBeforeCurrent(overviewFor, thisMonth, thisYear))
//            	{
//            		sb.append("<td class='noMonthBefore'>" );
//            	}
//            	else
//            	{
//            		sb.append("<td class='noMonthAfter'>" );
//            	}
//            	
//            	sb.append(HTML_NBSP + HTML_CELL_CLOSE);
	        }
	    }		
	}
	
	/**
	 * Add row with day numbers
	 * @param sb
	 * @param calendar
	 * @param thisMonth
	 */
	private void addDayNumbersToRow(WebMarkupContainer row)
	{
		for (String dayId : DateUtil.daysInWeek)
	    {
			Label	dayLabel;
			
			if (overviewFor.get(Calendar.MONTH) == thisMonth)
			{
				dayLabel = new Label(dayId, Integer.toString(overviewFor.get(Calendar.DAY_OF_MONTH)));
			}
			else
			{
				dayLabel = HtmlUtil.getNbspLabel(dayId);
				
				if (!monthIsBeforeCurrent(overviewFor, thisMonth, thisYear))
				{
					dayLabel.add(new SimpleAttributeModifier("class", "noMonth"));
				}
			}
			
			row.add(dayLabel);

			overviewFor.add(Calendar.DATE, 1);
	    }
		
		// reset the abused calendar
		overviewFor.add(Calendar.DATE, -7);
	}	
	
	/**
	 * 
	 * @param calendar
	 * @param thisMonth
	 * @param thisYear
	 * @return
	 */
	private boolean monthIsBeforeCurrent(Calendar calendar, int thisMonth, int thisYear)
	{
		int	year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		
		return month < thisMonth && year == thisYear ||
			   year < thisYear;
	}	
	
	/**
	 * Add day labels
	 * @param parent
	 */
	private void addDayLabels(WebMarkupContainer parent, EhourConfig config)
	{
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		
		for (String day : DateUtil.daysInWeek)
		{
			parent.add(new Label(day, new DateModel(cal, config, DateModel.DATESTYLE_TIMESHEET_DAYONLY)));
			cal.add(Calendar.DAY_OF_WEEK, 1);
		}
	}
}
