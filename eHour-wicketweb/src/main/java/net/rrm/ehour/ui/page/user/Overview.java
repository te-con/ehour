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

import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.overview.monthoverview.MonthOverviewPanel;
import net.rrm.ehour.ui.panel.overview.projectoverview.ProjectOverviewPanel;
import net.rrm.ehour.ui.panel.timesheet.TimesheetPanel;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.CommonUIStaticData;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.ResourceModel;
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
		super(new ResourceModel("overview.title"), null);
		
		session = ((EhourWebSession)this.getSession());
		
		user = session.getUser().getUser();

		// add calendar panel
		calendarPanel = new CalendarPanel("sidePanel", user);
		add(calendarPanel);
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));
		
		// content
		contentContainer = new WebMarkupContainer("contentContainer");
		
		// project overview panel
		contentContainer = getOverviewPanels();
		
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
			case CommonUIStaticData.AJAX_CALENDARPANEL_MONTH_CHANGE:
				calendarChanged(target);
				break;
			case CommonUIStaticData.AJAX_CALENDARPANEL_WEEK_CLICK:
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
			replacementPanel = getOverviewPanels();
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
	private WebMarkupContainer getOverviewPanels()
	{
		WebMarkupContainer container = new WebMarkupContainer("contentContainer");
		TimesheetOverview timesheetOverview = timesheetService.getTimesheetOverview(user, session.getNavCalendar());
		
		container.add(new ProjectOverviewPanel("projectOverview", timesheetOverview.getProjectStatus()));
		container.add(new MonthOverviewPanel("monthOverview", timesheetOverview));
		
		return container;
	}
}
