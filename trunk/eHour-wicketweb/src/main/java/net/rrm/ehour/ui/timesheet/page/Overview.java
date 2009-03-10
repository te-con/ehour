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

package net.rrm.ehour.ui.timesheet.page;

import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.page.BasePage;
import net.rrm.ehour.ui.common.panel.calendar.CalendarAjaxEventType;
import net.rrm.ehour.ui.common.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.common.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.common.TimesheetAjaxEventType;
import net.rrm.ehour.ui.timesheet.panel.OverviewPanel;
import net.rrm.ehour.ui.timesheet.panel.TimesheetPanel;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.ResourceModel;

/**
 * Overview page
 */

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class Overview extends BasePage
{
	private static final long serialVersionUID = -6873845464139697303L;

	public enum OpenPanel { OVERVIEW, TIMESHEET };
	
	private	WebMarkupContainer	contentContainer; // yeah yeah, bad name
	private CalendarPanel		calendarPanel;
	private ContextualHelpPanel	helpPanel;
	
	/**
	 * Setup the page
	 *
	 */
	public Overview()
	{
		this(OpenPanel.OVERVIEW);
	}
	
	/**
	 * 
	 * @param panelToOpen
	 */
	public Overview(OpenPanel panelToOpen)
	{
		super(new ResourceModel("overview.title"), null);
		
		// add calendar panel
		calendarPanel = new CalendarPanel("sidePanel", getEhourWebSession().getUser().getUser());
		add(calendarPanel);
		
		if (panelToOpen == OpenPanel.OVERVIEW)
		{
			// contextual help
			helpPanel = new ContextualHelpPanel("contextHelp", "overview.help.header", "overview.help.body");
			
			// content
			contentContainer = new OverviewPanel("contentContainer");
			
		}
		else
		{
			helpPanel = getTimesheetHelpPanel();
			contentContainer = getTimesheetPanel();
		}

		add(helpPanel);
		add(contentContainer);
	}	
	
	/**
	 * Handle Ajax request
	 * @param target
	 */
	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		AjaxEventType type = ajaxEvent.getEventType();
		AjaxRequestTarget target = ajaxEvent.getTarget();
		
		if (type == CalendarAjaxEventType.MONTH_CHANGE)
		{
			calendarChanged(target);
		}
		else if (type == CalendarAjaxEventType.WEEK_CLICK 
					|| type == TimesheetAjaxEventType.WEEK_NAV)
		{
			calendarWeekClicked(target);
			calendarPanel.setHighlightWeekStartingAt(DateUtil.getDateRangeForWeek(EhourWebSession.getSession().getNavCalendar()));
			calendarPanel.refreshCalendar(target);
		}
		else if (type == TimesheetAjaxEventType.TIMESHEET_SUBMIT)
		{
			calendarPanel.refreshCalendar(target);
		}
		
		return false;
	}
	
	/**
	 * Calendar week clicked
	 * @param target
	 */
	private void calendarWeekClicked(AjaxRequestTarget target)
	{
		TimesheetPanel	panel = getTimesheetPanel();
		
		contentContainer.replaceWith(panel);
		contentContainer = panel;

		target.addComponent(panel);
		
		ContextualHelpPanel replacementHelp = getTimesheetHelpPanel();
		helpPanel.replaceWith(replacementHelp);
		helpPanel = replacementHelp;
		target.addComponent(replacementHelp);
		
	}
	
	/**
	 * Calendar changed, update panels
	 * @param target
	 */
	private void calendarChanged(AjaxRequestTarget target)
	{
		WebMarkupContainer	replacementPanel;
		
		if (this.get("contentContainer") instanceof TimesheetPanel)
		{
			replacementPanel = getTimesheetPanel();
		}
		else
		{
			replacementPanel = new OverviewPanel("contentContainer");
		}
		
		contentContainer.replaceWith(replacementPanel);
		contentContainer = replacementPanel;
		target.addComponent(replacementPanel);
	}

	/**
	 * Get timesheet panel for current user & current month
	 * @return
	 */
	private TimesheetPanel getTimesheetPanel()
	{
		return new TimesheetPanel("contentContainer", 
					getEhourWebSession().getUser().getUser(), 
					getEhourWebSession().getNavCalendar());
	}
	
	/**
	 * 
	 * @return
	 */
	private ContextualHelpPanel getTimesheetHelpPanel()
	{
		ContextualHelpPanel helpPanel = new ContextualHelpPanel("contextHelp", 
				"timesheet.help.header",
				"timesheet.help.body");
		
		helpPanel.setOutputMarkupId(true);
		
		return helpPanel;
		
	}
}
