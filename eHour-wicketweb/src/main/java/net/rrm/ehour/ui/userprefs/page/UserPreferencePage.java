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

package net.rrm.ehour.ui.userprefs.page;

import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.page.AbstractBasePage;
import net.rrm.ehour.ui.common.panel.calendar.CalendarAjaxEventType;
import net.rrm.ehour.ui.common.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.common.TimesheetAjaxEventType;
import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage;
import net.rrm.ehour.ui.userprefs.panel.ChangePasswordBackingBean;
import net.rrm.ehour.ui.userprefs.panel.ChangePasswordPanel;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.ResourceModel;

/**
 * User preference page 
 **/
@AuthorizeInstantiation({UserRole.ROLE_USER, UserRole.ROLE_ADMIN, UserRole.ROLE_REPORT})
public class UserPreferencePage extends AbstractBasePage<Void>
{
	public UserPreferencePage() throws ObjectNotFoundException
	{
		super(new ResourceModel("userprefs.title"));
		
		// add calendar panel
		add(new CalendarPanel("sidePanel", EhourWebSession.getUser()));

		// add 
		add(new ChangePasswordPanel("preferenceForm", new ChangePasswordBackingBean()));
	}
	
	@Override
	public Boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		AjaxEventType type = ajaxEvent.getEventType();
		
		if (type == CalendarAjaxEventType.WEEK_CLICK 
				|| type == TimesheetAjaxEventType.WEEK_NAV)
		{
			setResponsePage(new MonthOverviewPage(MonthOverviewPage.OpenPanel.TIMESHEET));
		}
		
		return false;
	}	
}
