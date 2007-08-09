/**
 * Created on Jul 20, 2007
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

package net.rrm.ehour.ui.page.admin.user;

import java.util.List;

import net.rrm.ehour.ui.page.admin.BaseAdminPage;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.util.CommonStaticData;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Employee management page 
 **/

public class UserAdmin extends BaseAdminPage
{

	@SpringBean
	private	UserService	userService;
	private	ListView	userListView;
	private	transient Logger	logger = Logger.getLogger(UserAdmin.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1883278850247747252L;

	public UserAdmin()
	{
		super(new ResourceModel("admin.user.title"), null);
		
		List<User>	users;
		
		users = getUsers(null, false);
		userListView = getUserListView(users);
		userListView.setOutputMarkupId(true);
		
		add(new EntrySelectorPanel("userSelector",
				new ResourceModel("admin.user.title"),
				userListView,
				getLocalizer().getString("admin.user.filter", this) + "...",
				getLocalizer().getString("admin.user.hideInactive", this)));
	}
	
	/**
	 * Handle Ajax request
	 * @param target
	 * @param type of ajax req
	 */
	@Override
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object param)
	{
		if (type == CommonStaticData.AJAX_ENTRYSELECTOR_FILTER_CHANGE)
		{
			EntrySelectorFilter filter = (EntrySelectorFilter)param;

			if (logger.isDebugEnabled())
			{
				logger.debug("Filtering on " + filter.getCleanFilterInput() + ", hide active: " + filter.isActivateToggle());
			}
			
			List<User> users = getUsers(filter.getCleanFilterInput(), filter.isActivateToggle());
			userListView.setList(users);
		}
	}
	
	/**
	 * 
	 * @param users
	 * @return
	 */
	private ListView getUserListView(List<User> users)
	{
		return new ListView("itemList", users)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				final User	user = (User)item.getModelObject();
				
				item.add(new Label("item", 
						user.getLastName() + ", " + user.getFirstName() + (user.isActive() ? "" : "*")));
			}
		};
	}
	
	/**
	 * 
	 * @param filter
	 * @param hideInactive
	 * @return
	 */
	private List<User> getUsers(String filter, boolean hideInactive)
	{
		List<User>	users;
		
		if (filter == null)
		{
			users = userService.getUsers();
		}
		else
		{
			users = userService.getUsersByNameMatch(filter, hideInactive);
		}
		
		return users;
	}
}
