/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.timesheet.page;

import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
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
public class MonthOverviewPage extends BasePage
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
	public MonthOverviewPage()
	{
		this(OpenPanel.OVERVIEW);
	}
	
	/**
	 * 
	 * @param panelToOpen
	 */
	public MonthOverviewPage(OpenPanel panelToOpen)
	{
		super(new ResourceModel("overview.title"), null);
		
		// add calendar panel
		calendarPanel = new CalendarPanel("sidePanel", getEhourWebSession().getUser().getUser());
		add(calendarPanel);
		
		if (panelToOpen == OpenPanel.OVERVIEW)
		{
			// contextual help
			helpPanel = new ContextualHelpPanel("contextHelp", "overview.help.header", "overview.help.body", "Month+overview");
			
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
				"timesheet.help.body",
				"Entering+hours");
		
		helpPanel.setOutputMarkupId(true);
		
		return helpPanel;
		
	}
}
