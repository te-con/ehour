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

package net.rrm.ehour.ui.page.user;

import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.overview.OverviewPanel;
import net.rrm.ehour.ui.panel.timesheet.TimesheetPanel;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonWebUtil;
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

	private	WebMarkupContainer	contentContainer; // yeah yeah, bad name
	private CalendarPanel		calendarPanel;
	private ContextualHelpPanel	helpPanel;
	
	/**
	 * Setup the page
	 *
	 */
	public Overview()
	{
		super(new ResourceModel("overview.title"), null);
		
		// add calendar panel
		calendarPanel = new CalendarPanel("sidePanel", getEhourWebSession().getUser().getUser());
		add(calendarPanel);
		
		// contextual help
		helpPanel = new ContextualHelpPanel("contextHelp", "overview.help.header", "overview.help.body");
		helpPanel.setOutputMarkupId(true);
		add(helpPanel);
		
		// content
		contentContainer = new OverviewPanel("contentContainer");
		
		add(contentContainer);
	}
	
	/**
	 * Handle Ajax request
	 * @param target
	 */
	@Override
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		switch (type)
		{
			case CommonWebUtil.AJAX_CALENDARPANEL_MONTH_CHANGE:
				calendarChanged(target);
				break;
			case CommonWebUtil.AJAX_CALENDARPANEL_WEEK_NAV:
			case CommonWebUtil.AJAX_CALENDARPANEL_WEEK_CLICK:
				calendarWeekClicked(target);
				calendarPanel.setHighlightWeekStartingAt(DateUtil.getDateRangeForWeek(EhourWebSession.getSession().getNavCalendar()));
				calendarPanel.refreshCalendar(target);
				break;
			case CommonWebUtil.AJAX_FORM_SUBMIT:
				calendarPanel.refreshCalendar(target);
				break;
				
		}
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
		
		ContextualHelpPanel replacementHelp = new ContextualHelpPanel("contextHelp", 
																		"timesheet.help.header",
																		"timesheet.help.body");
		replacementHelp.setOutputMarkupId(true);
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
	protected TimesheetPanel getTimesheetPanel()
	{
		return new TimesheetPanel("contentContainer", 
					getEhourWebSession().getUser().getUser(), 
					getEhourWebSession().getNavCalendar());
	}
}
