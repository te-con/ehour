/**
 * Created on Oct 24, 2008
 * Author: Thies
 *
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

package net.rrm.ehour.ui.userprefs.page;

import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.page.BasePage;
import net.rrm.ehour.ui.common.panel.calendar.CalendarAjaxEventType;
import net.rrm.ehour.ui.common.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.timesheet.common.TimesheetAjaxEventType;
import net.rrm.ehour.ui.timesheet.page.Overview;
import net.rrm.ehour.ui.userprefs.panel.UserPasswordChangePanel;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.ResourceModel;

/**
 * User preference page 
 **/
@AuthorizeInstantiation({"ROLE_CONSULTANT", "ROLE_ADMIN", "ROLE_REPORT"})
public class UserPreferencePage extends BasePage
{
	private CalendarPanel		calendarPanel;
	
	public UserPreferencePage() throws ObjectNotFoundException
	{
		super(new ResourceModel("userprefs.title"));
		
		// add calendar panel
		calendarPanel = new CalendarPanel("sidePanel", getEhourWebSession().getUser().getUser());
		add(calendarPanel);
		
		// add 
		add(new UserPasswordChangePanel("preferenceForm", getEhourWebSession().getUser().getUser()));
	}
	
	/**
	 * Handle Ajax request
	 * @param target
	 */
	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		AjaxEventType type = ajaxEvent.getEventType();
		
		if (type == CalendarAjaxEventType.WEEK_CLICK 
				|| type == TimesheetAjaxEventType.WEEK_NAV)
		{
			setResponsePage(new Overview(Overview.OpenPanel.TIMESHEET));
		}
		
		return false;
	}	
}
