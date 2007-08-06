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
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.service.UserService;

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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1883278850247747252L;

	public UserAdmin()
	{
		super(new ResourceModel("admin.user.title"), null);
		
		List<User>	users;
		ListView	userListView;
		users = getUsers(null, false);
		userListView = getUserListView(users);
		
		add(new EntrySelectorPanel("userSelector",
									new ResourceModel("admin.user.title"),
									getLocalizer().getString("admin.user.filter", this),
									null, userListView));
	}
	
	private ListView getUserListView(List<User> users)
	{
		return new ListView("itemList", users)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				final User	user = (User)item.getModelObject();
				
				item.add(new Label("item", user.getLastName()));
			}
		};
	}
	
	
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
