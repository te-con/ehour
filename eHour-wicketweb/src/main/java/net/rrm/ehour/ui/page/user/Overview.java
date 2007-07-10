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

package net.rrm.ehour.ui.page.user;

import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.panel.overview.projectoverview.ProjectOverviewPanel;
import net.rrm.ehour.ui.panel.timesheet.TimesheetPanel;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonStaticData;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Overview page
 */

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class Overview extends BasePage
{
	private static final long serialVersionUID = -6873845464139697303L;

	@SpringBean
	private TimesheetService	timesheetService;
	private CalendarPanel		calendarPanel;
	private	WebMarkupContainer	contentContainer; // yeah yeah, bad name
	private	User				user;
	private EhourWebSession		session;
	
	/**
	 * Setup the page
	 *
	 */
	public Overview()
	{
		super("overview", null);
		
		session = ((EhourWebSession)this.getSession());
		
		user = session.getUser().getUser();

		// add calendar panel
		calendarPanel = new CalendarPanel("sidePanel", user);
		add(calendarPanel);
		
		contentContainer = new WebMarkupContainer("contentContainer");
		
		// project overview panel
		contentContainer = getProjectOverviewPanel();
		
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
			case CommonStaticData.AJAX_CALENDARPANEL_MONTH_CHANGE:
				calendarChanged(target);
				break;
			case CommonStaticData.AJAX_CALENDARPANEL_WEEK_CLICK:
				calendarWeekClicked(target);
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
	}
	
	/**
	 * Calendar changed, update panels
	 * @param target
	 */
	private void calendarChanged(AjaxRequestTarget target)
	{
		CalendarPanel panel = new CalendarPanel("sidePanel", user);
		calendarPanel.replaceWith(panel);
		calendarPanel = panel;
		
		target.addComponent(panel);

		WebMarkupContainer	replacementPanel;
		
		if (this.get("contentContainer") instanceof TimesheetPanel)
		{
			replacementPanel = getTimesheetPanel();
		}
		else
		{
			replacementPanel = getProjectOverviewPanel();
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
		return new TimesheetPanel("contentContainer", user, session.getNavCalendar());
	}
	
	/**
	 * Get project overview panel for current user for current month
	 * @return
	 */
	private ProjectOverviewPanel getProjectOverviewPanel()
	{
		TimesheetOverview timesheetOverview = timesheetService.getTimesheetOverview(user, session.getNavCalendar());
		
		return new ProjectOverviewPanel("contentContainer", timesheetOverview.getProjectStatus());
	}
}
