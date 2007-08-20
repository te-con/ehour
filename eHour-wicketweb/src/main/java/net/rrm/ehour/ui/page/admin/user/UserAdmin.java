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

import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.panel.admin.user.form.UserFormPanel;
import net.rrm.ehour.ui.panel.admin.user.form.dto.UserBackingBean;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.util.CommonStaticData;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.EhourConstants;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * User management page using 2 tabs, an entrySelector panel and the UserForm panel 
 **/

public class UserAdmin extends BaseTabbedAdminPage
{
	private final String	USER_SELECTOR_ID = "userSelector";
	
	@SpringBean
	private	UserService				userService;
	private	ListView				userListView;
	private	transient 	Logger		logger = Logger.getLogger(UserAdmin.class);
	private EntrySelectorFilter		currentFilter;
	private List<UserRole>			roles ;
	private List<UserDepartment>	departments;
	
	private static final long serialVersionUID = 1883278850247747252L;

	/**
	 * 
	 */
	public UserAdmin()
	{
		super(new ResourceModel("admin.user.title"),
					new ResourceModel("admin.user.addUser"),
					new ResourceModel("admin.user.editUser"));
		
		List<User>	users;
		users = getUsers();
		
		Fragment userListHolder = getUserListHolder(users);
		
		add(new EntrySelectorPanel(USER_SELECTOR_ID,
				new ResourceModel("admin.user.title"),
				userListHolder,
				getLocalizer().getString("admin.user.filter", this) + "...",
				getLocalizer().getString("admin.user.hideInactive", this)));
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
						setEditBackingBean(new UserBackingBean(userService.getUser(userId)));
						switchTabOnAjaxTarget(target, 1);
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
	 * Handle Ajax request
	 * @param target
	 * @param type of ajax req
	 */
	@Override
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object param)
	{
		switch (type)
		{
			case CommonStaticData.AJAX_ENTRYSELECTOR_FILTER_CHANGE:
			{
				currentFilter = (EntrySelectorFilter)param;
	
				List<User> users = getUsers();
				userListView.setList(users);
				break;
			}
			case CommonStaticData.AJAX_FORM_SUBMIT:
			{
				UserBackingBean	backingBean = (UserBackingBean) ((((IWrapModel) param)).getWrappedModel()).getObject();
				try
				{
					persistUser(backingBean);

					// update user list
					List<User> users = getUsers();
					userListView.setList(users);
					
					((EntrySelectorPanel)get(USER_SELECTOR_ID)).refreshList(target);
					
					succesfulSave(target);
				} catch (Exception e)
				{
					logger.error("While persisting user", e);
					failedSave(backingBean, target);
				}
				
				break;
			}
		}
	}
	
	/**
	 * Persist user
	 * @param userBackingBean
	 */
	private void persistUser(UserBackingBean userBackingBean) throws Exception
	{
		logger.info(((userBackingBean == getEditBackingBean()) 
											? "Updating" 
											: "Adding") + " user :" + userBackingBean.getUser());
		
		if (userBackingBean.isPm())
		{
			logger.debug("Readding PM role after edit");
			userBackingBean.getUser().addUserRole(new UserRole(EhourConstants.ROLE_PROJECTMANAGER));
		}
		
		userService.persistUser(userBackingBean.getUser());
	}
	
	/**
	 * Get the users from the backend
	 * @return
	 */
	private List<User> getUsers()
	{
		List<User>	users;
		
		if (currentFilter == null)
		{
			users = userService.getUsers();
		}
		else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Filtering on " + currentFilter.getCleanFilterInput() + ", hide active: " + currentFilter.isActivateToggle());
			}
			
			users = userService.getUsersByNameMatch(currentFilter.getCleanFilterInput(), currentFilter.isActivateToggle());
		}
		
		return users;
	}


	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewAddBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewAddBackingBean()
	{
		UserBackingBean	userBean;
		
		userBean = new UserBackingBean(new User());
		userBean.getUser().setActive(true);

		return userBean;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BasedTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getAddPanel(String panelId)
	{
		return new UserFormPanel(panelId,
				new CompoundPropertyModel(getAddBackingBean()),
				getUserRoles(),
				getUserDepartments());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewEditBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewEditBackingBean()
	{
		return new UserBackingBean(new User());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BasedTabbedAdminPage#getEditPanel(java.lang.String)
	 */
	@Override
	protected Panel getEditPanel(String panelId)
	{
		return new UserFormPanel(panelId,
				new CompoundPropertyModel(getEditBackingBean()),
				getUserRoles(),
				getUserDepartments());
	}
	
	/**
	 * Get all the roles from the backend
	 * @return
	 */
	private List<UserRole> getUserRoles()
	{
		if (roles == null)
		{
			roles = userService.getUserRoles();
		}
		
		return roles;
	}

	/**
	 * Get all departments from the backend
	 * @return
	 */
	private List<UserDepartment> getUserDepartments()
	{
		if (departments == null)
		{
			departments = userService.getUserDepartments();
		}
		
		return departments;
	}
}
