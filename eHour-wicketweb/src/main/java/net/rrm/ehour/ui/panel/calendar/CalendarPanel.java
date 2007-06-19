/**
 * Created on May 8, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.sidepanel.SidePanel;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonStaticData;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Navigation Calendar 
 **/

public class CalendarPanel extends SidePanel
{
	private static final long serialVersionUID = -7777893083323915299L;

	private transient Logger logger = Logger.getLogger(CalendarPanel.class);

	@SpringBean
	private TimesheetService timesheetService;

	/**
	 * 
	 * @param id
	 */
	public CalendarPanel(String id, Integer userId)
	{
		super(id);
		
		Calendar month = ((EhourWebSession)this.getSession()).getNavCalendar();
		this.setOutputMarkupId(true);
		
		EhourWebSession 	session = (EhourWebSession)getSession();
		List<CalendarWeek>	weeks;
		
		logger.debug("Constructing navCalendar for userId: " + userId + " and month " + month.getTime().toString());
		
		// set month label
		add(new Label("currentMonth", new DateModel(month, session.getEhourConfig(), DateModel.DATESTYLE_MONTHONLY)));
		
		// previous & next month links
		AjaxLink previousMonthLink = new ChangeMonthLink("previousMonthLink", -1);
		previousMonthLink.add(new Image("previousMonthImg", new ResourceReference(CalendarPanel.class, "arrow_left.gif")));
		add(previousMonthLink);

		AjaxLink nextMonthLink = new ChangeMonthLink("nextMonthLink", 1);
		nextMonthLink.add(new Image("nextMonthImg", new ResourceReference(CalendarPanel.class, "arrow_right.gif")));
		add(nextMonthLink);

		// CSS
		add(new StyleSheetReference("calendarStyle", calendarStyle()));
		
		// content
		weeks = createWeeks(userId, month);
		addCalendarWeeks(this, weeks);
		
		logger.debug("Weeks filled: " + weeks.size());

	}
	

	/**
	 * Add the calendar weeks
	 * @param container
	 * @param weeks
	 */
	private void addCalendarWeeks(WebMarkupContainer container, List<CalendarWeek> weeks)
	{
		ListView view = new ListView("weeks", weeks)
		{
			public void populateItem(final ListItem item)
			{
				CalendarWeek week = (CalendarWeek) item.getModelObject();

				item.add(getLabel("sunday", week, 0));
				item.add(getLabel("monday", week, 1));
				item.add(getLabel("tuesday", week, 2));
				item.add(getLabel("wednesday", week, 3));
				item.add(getLabel("thursday", week, 4));
				item.add(getLabel("friday", week, 5));
				item.add(getLabel("saturday", week, 6));
				
		        item.setOutputMarkupId(true);
				item.add(new AjaxWeekBehaviour("onclick", week.getWeek(), week.getYear()));
			}

			private Label getLabel(String id, CalendarWeek week, int dayInWeek)
			{
				Label label = new Label(id, new PropertyModel(week, "days[" + dayInWeek + "]"));

				if (week.getDays()[dayInWeek] == 0)
				{
					label.setVisible(false);
				}
				else if (week.getDaysBooked()[dayInWeek])
				{
					label.add(new SimpleAttributeModifier("style", "font-weight: bold"));
				}

				return label;
			}
		};

	
		container.add(view);
	}

	/**
	 * Create CalendarWeek objects
	 * 
	 * @param userId
	 * @param month
	 */
	private List<CalendarWeek> createWeeks(Integer userId, Calendar month)
	{
		List<CalendarWeek> calendarWeeks = new ArrayList<CalendarWeek>();
		boolean[] bookedDays;
		CalendarWeek week = new CalendarWeek();
		int currentMonth;
		int dayInMonth;
		int dayInWeek;

		// grab date
		bookedDays = getMonthNavCalendar(userId, month);

		week.setWeek(month.get(Calendar.WEEK_OF_YEAR));
		week.setYear(month.get(Calendar.YEAR));

		currentMonth = month.get(Calendar.MONTH);

		month.set(Calendar.DAY_OF_MONTH, 1);

		do
		{
			dayInMonth = month.get(Calendar.DAY_OF_MONTH);
			dayInWeek = month.get(Calendar.DAY_OF_WEEK);

			week.setDayInWeek(dayInWeek - 1, dayInMonth, bookedDays[dayInMonth - 1]);

			month.add(Calendar.DAY_OF_MONTH, 1);

			// next week? restart a
			if (month.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			{
				calendarWeeks.add(week);

				week = new CalendarWeek();
				week.setWeek(month.get(Calendar.WEEK_OF_YEAR));
				week.setYear(month.get(Calendar.YEAR));
			}

		} while (month.get(Calendar.MONTH) == currentMonth);

		// sundays are already stored
		if (month.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
		{
			calendarWeeks.add(week);
		}

		return calendarWeeks;
	}

	/**
	 * Create a style
	 * 
	 * @return a style
	 */
	private ResourceReference calendarStyle()
	{
		return new CompressedResourceReference(CalendarPanel.class, "style/calendar.css");
	}

	/**
	 * Get the month overview for the nav calendar
	 * @param userId
	 * @param requestedMonth
	 * @return array of booleans each representing a day in the month. 
	 * true = booked, false = not everything booked
	 */

	private boolean[] getMonthNavCalendar(Integer userId, Calendar requestedMonth)
	{
		List<BookedDay> bookedDays;
		boolean[] monthOverview;
		Calendar cal;

		bookedDays = timesheetService.getBookedDaysMonthOverview(userId, requestedMonth);

		monthOverview = new boolean[DateUtil.getDaysInMonth(requestedMonth)];

		for (BookedDay day : bookedDays)
		{
			cal = new GregorianCalendar();
			cal.setTime(day.getDate());

			// just in case.. it shouldn't happen that the returned month
			// is longer than the reserved space for this month
			if (cal.get(Calendar.DAY_OF_MONTH) <= monthOverview.length)
			{
				monthOverview[cal.get(Calendar.DAY_OF_MONTH) - 1] = true;
			}
		}

		return monthOverview;
	}

	/**
	 * Changes the month
	 * @author Thies
	 *
	 */
	private class ChangeMonthLink extends AjaxLink
	{
		private	int monthChange;
		
		public ChangeMonthLink(String id, int monthChange)
		{
			super(id);
			
			this.monthChange = monthChange;
		}
		
		@Override
		public void onClick(AjaxRequestTarget target)
        {
			EhourWebSession session = (EhourWebSession)this.getSession(); 
			Calendar month = session.getNavCalendar();
			month.add(Calendar.MONTH, monthChange);
			session.setNavCalendar(month);

			((BasePage)getPage()).ajaxRequestReceived(target, CommonStaticData.AJAX_CALENDARPANEL_MONTH_CHANGE);
        }
		
		@Override
		protected IAjaxCallDecorator getAjaxCallDecorator()
		{
			return new LoadingSpinnerDecorator();
		}		
	}

	/**
	 * 
	 * @author Thies
	 *
	 */
	private class AjaxWeekBehaviour extends AjaxEventBehavior
	{
		private int week, year;
		
		public AjaxWeekBehaviour(String id, int week, int year)
		{
			super(id);
			this.week = week;
			this.year = year;
		}
		
		protected void onEvent(AjaxRequestTarget target)
		{
			System.out.println("ajax here on " + week + ", year " + year);
		}
	}				
}
