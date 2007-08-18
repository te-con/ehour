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

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.ui.page.admin.BaseAdminPage;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.panel.user.form.UserFormPanel;
import net.rrm.ehour.ui.panel.user.form.dto.UserBackingBean;
import net.rrm.ehour.ui.util.CommonStaticData;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
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
	private	transient 	Logger	logger = Logger.getLogger(UserAdmin.class);
	private	AbstractTab		addUserTab;
	private	AbstractTab		editUserTab;
	private	AjaxTabbedPanel	tabbedPanel;
	private	UserBackingBean	addUser;
	private	UserBackingBean	editUser;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1883278850247747252L;

	public UserAdmin()
	{
		super(new ResourceModel("admin.user.title"), null);
		
		List<User>	users;
		
		addUser = new UserBackingBean(new User());
		editUser = new UserBackingBean(new User());
		
		users = getUsers(null, false);
		Fragment userListHolder = getUserListHolder(users);
		
		add(new EntrySelectorPanel("userSelector",
				new ResourceModel("admin.user.title"),
				userListHolder,
				getLocalizer().getString("admin.user.filter", this) + "...",
				getLocalizer().getString("admin.user.hideInactive", this)));
		
		setUpTabs();
	}
	
	/**
	 * Setup tabs
	 */
	private void setUpTabs()
	{
		List<AbstractTab>	tabs = new ArrayList<AbstractTab>();
		final List<UserRole>		roles = userService.getUserRoles();
		final List<UserDepartment>	departments = userService.getUserDepartments();
		
		addUserTab = new AbstractTab(new ResourceModel("admin.user.addUser"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new UserFormPanel(panelId,
										new CompoundPropertyModel(addUser),
										roles,
										departments);
			}
			
		};
		tabs.add(addUserTab);

		editUserTab = new AbstractTab(new ResourceModel("admin.user.editUser"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new UserFormPanel(panelId,
											new CompoundPropertyModel(editUser),
											roles,
											departments);
			}
		};
		
		tabs.add(editUserTab);
		
		tabbedPanel = new AjaxTabbedPanel("tabs", tabs);
		add(tabbedPanel);
	}

	
	/**
	 * Get a the userListHolder fragment containing the listView
	 * @param users
	 * @return
	 */
	private Fragment getUserListHolder(List<User> users)
	{
		Fragment fragment = new Fragment("itemListHolder", "itemListHolder", UserAdmin.this);
		
		userListView = new ListView("itemList", users)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				final User		user = (User)item.getModelObject();
				final Integer	userId = user.getUserId();
				
				AjaxLink	link = new AjaxLink("itemLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
						editUser = new UserBackingBean(userService.getUser(userId));
						switchTab(target, 1, userId);
					}
				};
				
				item.add(link);
				link.add(new Label("linkLabel", user.getLastName() + ", " + user.getFirstName() + (user.isActive() ? "" : "*")));				
			}
		};
		
		fragment.add(userListView);
		
		return fragment;
	}
	
	/**
	 * Switch tab
	 * @param tab
	 * @param userId
	 */
	private void switchTab(AjaxRequestTarget target, int tab, Integer userId)
	{
		tabbedPanel.setSelectedTab(tab);
		target.addComponent(tabbedPanel);
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
	 * Get the users from the backend
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
