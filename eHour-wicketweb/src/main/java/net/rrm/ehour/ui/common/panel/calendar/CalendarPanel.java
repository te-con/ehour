/**
 * Created on May 8, 2007
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

package net.rrm.ehour.ui.common.panel.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxUtil;
import net.rrm.ehour.ui.common.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.ajax.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.component.DisablingAjaxLink;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.sidepanel.SidePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.HtmlUtil;
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
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Navigation Calendar
 **/

public class CalendarPanel extends SidePanel
{
	private static final long serialVersionUID = -7777893083323915299L;

	private final static Logger logger = Logger.getLogger(CalendarPanel.class);

	@SpringBean
	private TimesheetService 	timesheetService;
	private WebMarkupContainer	calendarFrame;
	private	User				user;
	private boolean				fireWeekClicks;
	private	DateRange			highlightWeekStartingAt;
	private EhourConfig			config;


	/**
	 * Calendar firing off week clicks as well
	 * @param id
	 */
	public CalendarPanel(String id, User user)
	{
		this(id, user, true);
	}

	/**
	 * Calendar with optional week click events
	 * @param id
	 * @param user
	 * @param allowWeekClicks
	 */
	public CalendarPanel(String id, User user, boolean allowWeekClicks)
	{
		super(id);

		setOutputMarkupId(true);

		EhourWebSession 	session = (EhourWebSession)getSession();
		config = session.getEhourConfig();

		this.user = user;
		fireWeekClicks = allowWeekClicks;

		//
		calendarFrame = getFrame();
		add(calendarFrame);

		buildCalendar(calendarFrame);
	}

	/**
	 * Refresh calendar
	 * @param target
	 */
	public void refreshCalendar(AjaxRequestTarget target)
	{
		WebMarkupContainer replacementFrame = getFrame();
		buildCalendar(replacementFrame);

		calendarFrame.replaceWith(replacementFrame);
		calendarFrame = replacementFrame;
		target.addComponent(replacementFrame);
	}

	/**
	 * Get frame
	 * @return
	 */
	private WebMarkupContainer getFrame()
	{
		WebMarkupContainer calendarFrame = new WebMarkupContainer("calendarFrame");
		calendarFrame.setOutputMarkupId(true);

		return calendarFrame;
	}

	/**
	 * Build calendar
	 * @param parent
	 * @param user
	 */
	private void buildCalendar(WebMarkupContainer parent)
	{
		// first get the data
		Calendar month = ((EhourWebSession)this.getSession()).getNavCalendar();
		List<CalendarWeek>	weeks;

		logger.debug("Constructing navCalendar for userId: " + user.getUserId() + " and month " + month.getTime().toString());
		weeks = createWeeks(user.getUserId(), ((GregorianCalendar)month.clone()));

		// set month label
		parent.add(new Label("currentMonth", new DateModel(month, ((EhourWebSession)getSession()).getEhourConfig(), DateModel.DATESTYLE_MONTHONLY)));

		// previous & next month links
		AjaxLink previousMonthLink = new ChangeMonthLink("previousMonthLink", -1);
		previousMonthLink.add(new Image("previousMonthImg", new ResourceReference(CalendarPanel.class, "arrow_left.gif")));
		parent.add(previousMonthLink);

		AjaxLink nextMonthLink = new ChangeMonthLink("nextMonthLink", 1);
		nextMonthLink.add(new Image("nextMonthImg", new ResourceReference(CalendarPanel.class, "arrow_right.gif")));
		parent.add(nextMonthLink);

		// content
		addCalendarWeeks(parent, weeks);
	}

	/**
	 * Add the calendar weeks
	 * @param container
	 * @param weeks
	 */
	@SuppressWarnings("serial")
	private void addCalendarWeeks(WebMarkupContainer container, List<CalendarWeek> weeks)
	{
		ListView view = new ListView("weeks", weeks)
		{
			public void populateItem(final ListItem item)
			{
				CalendarWeek week = (CalendarWeek) item.getModelObject();
				Calendar renderDate = (Calendar)week.getWeekStart().clone();

				for (int i = 1; i <= 7; i++)
				{
					boolean weekend = DateUtil.isWeekend(renderDate);

					int currentDay = renderDate.get(Calendar.DAY_OF_WEEK);
					CalendarDay day = week.getDay(currentDay);
					
					Label label = getLabel(i, week, day, weekend);
					item.add(label);
					
					renderDate.add(Calendar.DATE, 1);
				};

		        item.setOutputMarkupId(true);

		        if (fireWeekClicks)
		        {
		        	if (highlightWeekStartingAt == null ||
		        			!DateUtil.isDateWithinRange(week.getWeekStart().getTime(), highlightWeekStartingAt))
        			{
						item.add(new WeekClick("onclick", week.getWeek(), week.getYear()));
						item.add(new SimpleAttributeModifier("onmouseover", "backgroundOn(this)"));
						item.add(new SimpleAttributeModifier("onmouseout", "backgroundOff(this)"));
		        	}
		        }
		        else
		        {
		        	item.add(new SimpleAttributeModifier("style", "cursor:default"));
		        }
			}

			/**
			 *
			 * @param id
			 * @param week
			 * @param dayInWeek
			 * @return
			 */
			private Label getLabel(int i, CalendarWeek week, CalendarDay day, boolean weekend)
			{
				Label 	label;
				String	id = "day" + i;

				// when day is null the date is in the next/previous month
				if (day == null)
				{
					label = HtmlUtil.getNbspLabel(id);
				}
				else
				{
					label = new Label(id, new PropertyModel(day, "monthDay"));
				}

				// determine css class
				String cssClass = weekend ? "WeekendDay" : "WeekDay";
				label.add(new SimpleAttributeModifier("class", cssClass));
				
				// determine custom css properties
				StringBuilder style = new StringBuilder();

				// booked days are bold
				if (day != null && day.isBooked())
				{
					style.append("font-weight: bold;");
				}
				
				// first day doesn't have margin-left 
				if (i > 1)
				{
					style.append("margin-left: 1px;");
				}

				// selected weeks have a more dark background
	        	if (highlightWeekStartingAt != null &&
	        			DateUtil.isDateWithinRange(week.getWeekStart().getTime(), highlightWeekStartingAt))
    			{
	        		style.append("background-color: #edf5fe;");
    			}

	        	if (style.length() > 0)
	        	{
	        		label.add(new SimpleAttributeModifier("style", style.toString()));
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
	 * @param dateIterator
	 */
	private List<CalendarWeek> createWeeks(Integer userId, Calendar dateIterator)
	{
		List<CalendarWeek> calendarWeeks = new ArrayList<CalendarWeek>();
		boolean[] bookedDays;
		CalendarWeek week;

		// grab date
		bookedDays = getMonthNavCalendar(userId, dateIterator);

		dateIterator.set(Calendar.DAY_OF_MONTH, 1);
		int currentMonth = dateIterator.get(Calendar.MONTH);
		
		week = new CalendarWeek();
		week.setWeek(dateIterator.get(Calendar.WEEK_OF_YEAR));
		week.setYear(dateIterator.get(Calendar.YEAR));
		week.setWeekStart((Calendar)dateIterator.clone());
		
		DateUtil.dayOfWeekFix(week.getWeekStart());
		week.getWeekStart().set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
		
		int previousWeek = -1;

		do
		{
			int dayInMonth = dateIterator.get(Calendar.DAY_OF_MONTH);
			int dayInWeek = dateIterator.get(Calendar.DAY_OF_WEEK);
			boolean inWeekend = (dayInWeek == Calendar.SUNDAY || dayInWeek == Calendar.SATURDAY); 
			
			CalendarDay day = new CalendarDay(dayInMonth, bookedDays[dayInMonth - 1], inWeekend);
			
			week.addDayInWeek(dayInWeek, day);

			dateIterator.add(Calendar.DAY_OF_MONTH, 1);

			// next week? add current week and create a new one
			if (dateIterator.get(Calendar.DAY_OF_WEEK) == config.getFirstDayOfWeek())
			{
				calendarWeeks.add(week);

				week = new CalendarWeek();
				week.setWeek(dateIterator.get(Calendar.WEEK_OF_YEAR));
				week.setWeekStart((Calendar)dateIterator.clone());

				// fix that the year is still the old year but the week is already in the next year
				if (previousWeek != -1 && previousWeek > dateIterator.get(Calendar.WEEK_OF_YEAR))
				{
					week.setYear(dateIterator.get(Calendar.YEAR) + 1);
				}
				else
				{
					week.setYear(dateIterator.get(Calendar.YEAR));
				}

				previousWeek = dateIterator.get(Calendar.WEEK_OF_YEAR);
			}

		} while (dateIterator.get(Calendar.MONTH) == currentMonth);

		// first day of week is already stored
		if (dateIterator.get(Calendar.DAY_OF_WEEK) != config.getFirstDayOfWeek())
		{
			calendarWeeks.add(week);
		}

		return calendarWeeks;
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
	private class ChangeMonthLink extends DisablingAjaxLink
	{
		private static final long serialVersionUID = 1L;
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
			month.set(Calendar.DAY_OF_MONTH, 1);
			session.setNavCalendar(month);

			// do it before it gets replaced, otherwise getPage is  null due to new instantiation of links
			AjaxUtil.publishAjaxEvent(ChangeMonthLink.this, new AjaxEvent(target, CalendarAjaxEventType.MONTH_CHANGE));

			refreshCalendar(target);

			this.setEnabled(false);
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
	private class WeekClick extends AjaxEventBehavior
	{
		private static final long serialVersionUID = 9164386260367481606L;

		private int week, year;

		public WeekClick(String id, int week, int year)
		{
			super(id);
			this.week = week;
			this.year = year;
		}

		@Override
		protected void onEvent(AjaxRequestTarget target)
		{
			EhourWebSession 	session = (EhourWebSession)getSession();
			Calendar cal = DateUtil.getCalendar(session.getEhourConfig());

			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.WEEK_OF_YEAR, week);
			DateUtil.dayOfWeekFix(cal);
			cal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());

			session.setNavCalendar(cal);

			AjaxUtil.publishAjaxEvent(CalendarPanel.this, new PayloadAjaxEvent<Calendar>(target, CalendarAjaxEventType.WEEK_CLICK, cal));
		}

		@Override
		protected IAjaxCallDecorator getAjaxCallDecorator()
		{
			return new LoadingSpinnerDecorator();
		}
	}

	public DateRange getHighlightWeekStartingAt()
	{
		return highlightWeekStartingAt;
	}

	public void setHighlightWeekStartingAt(DateRange highlightWeekStartingAt)
	{
		this.highlightWeekStartingAt = highlightWeekStartingAt;
	}
}
