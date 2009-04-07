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

package net.rrm.ehour.ui.admin.user.page;

import java.util.List;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.admin.user.dto.UserBackingBean;
import net.rrm.ehour.ui.admin.user.panel.UserAdminFormPanel;
import net.rrm.ehour.ui.admin.user.panel.UserEditAjaxEventType;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.ajax.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorAjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * User management page using 2 tabs, an entrySelector panel and the UserForm panel 
 **/

public class UserAdmin extends BaseTabbedAdminPage
{
	private	final static Logger		logger = Logger.getLogger(UserAdmin.class);

	@SpringBean
	private	UserService				userService;
	private	ListView				userListView;
	private EntrySelectorFilter		currentFilter;
	private List<UserRole>			roles ;
	private List<UserDepartment>	departments;
	private EntrySelectorPanel		selectorPanel;
	
	private static final long serialVersionUID = 1883278850247747252L;

	/**
	 * 
	 */
	public UserAdmin()
	{
		super(new ResourceModel("admin.user.title"),
					new ResourceModel("admin.user.addUser"),
					new ResourceModel("admin.user.editUser"),
					new ResourceModel("admin.user.noEditEntrySelected"),
					"admin.user.help.header",
					"admin.user.help.body");
		
		List<User>	users;
		users = getUsers();
		
		Fragment userListHolder = getUserListHolder(users);
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", 
															new ResourceModel("admin.user.title"), 
															EntrySelectorPanel.ENTRYSELECTOR_WIDTH);
		add(greyBorder);		
		
		selectorPanel = new EntrySelectorPanel("userSelector",
						userListHolder,
						new StringResourceModel("admin.user.filter", this, null),
						new ResourceModel("admin.user.hideInactive"));
		greyBorder.add(selectorPanel);
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
			private static final long serialVersionUID = 5334338761736798802L;

			@Override
			protected void populateItem(ListItem item)
			{
				final User		user = (User)item.getModelObject();
				final Integer	userId = user.getUserId();
				
				AjaxLink	link = new AjaxLink("itemLink")
				{
					private static final long serialVersionUID = -3898942767521616039L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						try
						{
							getTabbedPanel().setEditBackingBean(new UserBackingBean(userService.getUserAndCheckDeletability(userId)));
							getTabbedPanel().switchTabOnAjaxTarget(target, 1);
						} catch (ObjectNotFoundException e)
						{
							logger.error("User not found for id: " + userId);
						}

					}
				};
				
				item.add(link);
				link.add(new Label("linkLabel", user.getFullName() + (user.isActive() ? "" : "*")));				
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
	@SuppressWarnings("unchecked")
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		AjaxEventType type = ajaxEvent.getEventType();

		if (type == EntrySelectorAjaxEventType.FILTER_CHANGE)
		{
			currentFilter = ((PayloadAjaxEvent<EntrySelectorFilter>)ajaxEvent).getPayload();

			List<User> users = getUsers();
			userListView.setList(users);
			
			return false;
		}
		else if (type == UserEditAjaxEventType.USER_UPDATED
				|| type == UserEditAjaxEventType.USER_DELETED)
		{
			// update user list
			List<User> users = getUsers();
			userListView.setList(users);
			
			selectorPanel.refreshList(ajaxEvent.getTarget());
			
			getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
			
			return false;
		}
		
		return true;
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
	 * @see net.rrm.ehour.ui.admin.BasedTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseAddPanel(String panelId)
	{
		return new UserAdminFormPanel(panelId,
				new CompoundPropertyModel(getTabbedPanel().getAddBackingBean()),
				getUserRoles(),
				getUserDepartments());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BaseTabbedAdminPage#getNewAddBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewAddBaseBackingBean()
	{
		UserBackingBean	userBean;
		
		userBean = new UserBackingBean(new User());
		userBean.getUser().setActive(true);

		return userBean;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BaseTabbedAdminPage#getNewEditBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewEditBaseBackingBean()
	{
		return new UserBackingBean(new User());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BasedTabbedAdminPage#getEditPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseEditPanel(String panelId)
	{
		return new UserAdminFormPanel(panelId,
				new CompoundPropertyModel(getTabbedPanel().getEditBackingBean()),
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
