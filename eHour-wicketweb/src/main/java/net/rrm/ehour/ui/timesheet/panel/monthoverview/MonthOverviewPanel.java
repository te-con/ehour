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

package net.rrm.ehour.ui.timesheet.panel.monthoverview;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.TooltipLabel;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.ui.timesheet.export.ExportMonthSelectionPage;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
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
	public MonthOverviewPanel(String id, TimesheetOverview timesheetOverview, final Calendar overviewForMonth)
	{
		super(id);

		setOutputMarkupId(true);
		
		EhourWebSession session = (EhourWebSession)getSession();
		config = session.getEhourConfig();
		
		this.timesheetOverview = timesheetOverview;
	    thisMonth = overviewForMonth.get(Calendar.MONTH);
	    thisYear = overviewForMonth.get(Calendar.YEAR);

	    this.overviewFor = (Calendar)overviewForMonth.clone();
	    DateUtil.dayOfWeekFix(overviewFor);
	    overviewFor.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());

		
		Link printLink = new Link("printLink")
		{
			private static final long serialVersionUID = 4816200369282788652L;

			@Override
			public void onClick()
			{
				setResponsePage(new ExportMonthSelectionPage(overviewForMonth));
			}
		};
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("greyFrame", 
												new ResourceModel("monthoverview.overview"),
												true,
												printLink, null,
												CommonWebUtil.GREYFRAME_WIDTH);
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		
		greyBorder.add(blueBorder);
		add(greyBorder);
		
		addDayLabels(blueBorder, session.getEhourConfig());
		
		createMonthCalendar(blueBorder);
	}

	/**
	 * Create month calendar
	 * @param parent
	 * @param calendar
	 */
	private void createMonthCalendar(WebMarkupContainer parent)
	{
	    logger.debug("Creating month overview calendar for " + overviewFor.getTime().toString());
	    
	    RepeatingView	calendarView = new RepeatingView("calendarView");
	    
	    while ((overviewFor.get(Calendar.YEAR) == thisYear) &&
	    		(overviewFor.get(Calendar.MONTH) <= thisMonth) || overviewFor.get(Calendar.YEAR) < thisYear)
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
		row.add(new Label("weekNumber", Integer.toString(overviewFor.get(Calendar.WEEK_OF_YEAR))));
		
		addDayNumbersToWeek(row);
		addDayValuesToWeek(row);
	}
	
	/**
	 * Add all the day values to the week
	 * @param row
	 */
	private void addDayValuesToWeek(WebMarkupContainer row)
	{
		for (int i = 1; i <= 7; i++, overviewFor.add(Calendar.DATE, 1))
		{
			String dayId = "day" + i + "Value";

			Fragment fragment;
			
	        if (overviewFor.get(Calendar.MONTH) == thisMonth)
	        {
				boolean isWeekend = DateUtil.isWeekend(overviewFor);

				List<TimesheetEntry> timesheetEntries = null;
	        	
	        	if (timesheetOverview.getTimesheetEntries() != null)
	        	{
	        		timesheetEntries = timesheetOverview.getTimesheetEntries().get(overviewFor.get(Calendar.DAY_OF_MONTH));
	        	}
	        	
	            if (timesheetEntries != null
	            		&& timesheetEntries.size() > 0)
	            {
	            	fragment = new Fragment(dayId, "showProjects", this);
	            	
	            	ListView projects = new ListView("projects", timesheetEntries)
	            	{
						private static final long serialVersionUID = 1L;

						/*
						 * (non-Javadoc)
						 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
						 */
						@Override
						protected void populateItem(ListItem item)
						{
							TimesheetEntry entry = (TimesheetEntry)item.getModelObject();

							item.add(createProjectCodeTooltip(entry));
	            			item.add(new Label("hours", new FloatModel(entry.getHours(), config)));
						}
	            	};
	            	
	            	fragment.add(projects);
	            }
	            else
	            {
	            	fragment = new Fragment(dayId, "noProjects", this);
	            }
	            
            	if (isWeekend)
            	{
            		fragment.add(new SimpleAttributeModifier("style", "background-color: #eef6fe"));
            	}	            
	            row.add(fragment);
	        }
	        else
	        {
	        	Label label = HtmlUtil.getNbspLabel(dayId);
	        	
            	if (monthIsBeforeCurrent(overviewFor, thisMonth, thisYear))
            	{
            		label.add(new SimpleAttributeModifier("class", "noMonthBefore"));
            	}
            	else
            	{
            		label.add(new SimpleAttributeModifier("class", "noMonthAfter"));
            	}

            	row.add(label);
	        }
		}		
	}
	
	private TooltipLabel createProjectCodeTooltip(TimesheetEntry entry)
	{
		StringBuilder tooltipText;
		
		if (entry.getEntryId().getProjectAssignment().getProject().getDescription() != null)
		{
			tooltipText = new StringBuilder(entry.getEntryId().getProjectAssignment().getProject().getDescription());
		}
		else
		{
			tooltipText = new StringBuilder(CommonWebUtil.getResourceModelString(new ResourceModel("general.noDesc")));
		}

		tooltipText.append("<br />");
		
		tooltipText.append("<em>");
		tooltipText.append(entry.getEntryId().getProjectAssignment().getProject().getName());
		tooltipText.append("</em>");

		TooltipLabel projectCodeLabel = new TooltipLabel("projectCode", entry.getEntryId().getProjectAssignment().getProject().getProjectCode(),
				tooltipText.toString(), false);
		
		return projectCodeLabel;
	}
	
	/**
	 * Add row with day numbers
	 * @param sb
	 * @param calendar
	 * @param thisMonth
	 */
	private void addDayNumbersToWeek(WebMarkupContainer row)
	{
		for (int i = 1; i <= 7; i++, overviewFor.add(Calendar.DATE, 1))
	    {
			Label	dayLabel;
			String id = "day" + i;
			
			// 
			if (overviewFor.get(Calendar.MONTH) == thisMonth)
			{
				dayLabel = new Label(id, Integer.toString(overviewFor.get(Calendar.DAY_OF_MONTH)));
			}
			// print space holders if not current month
			else
			{
				dayLabel = HtmlUtil.getNbspLabel(id);
				
				if (!monthIsBeforeCurrent(overviewFor, thisMonth, thisYear))
				{
					dayLabel.add(new SimpleAttributeModifier("class", "noMonth"));
				}
			}
			
			row.add(dayLabel);
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
		cal.setFirstDayOfWeek(config.getFirstDayOfWeek());
		cal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
		
		for (int i = 1; i <= 7; i++, cal.add(Calendar.DAY_OF_WEEK, 1))
		{
			parent.add(new Label("day" + i, new DateModel(cal, config, DateModel.DATESTYLE_TIMESHEET_DAYONLY)));
		}
	}
}
