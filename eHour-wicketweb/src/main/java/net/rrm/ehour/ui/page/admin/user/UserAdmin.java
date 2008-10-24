/**
 * Created on Jul 20, 2007
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

package net.rrm.ehour.ui.page.admin.user;

import java.util.List;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.panel.admin.user.form.UserFormPanel;
import net.rrm.ehour.ui.panel.admin.user.form.dto.UserBackingBean;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.util.CommonWebUtil;
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
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * User management page using 2 tabs, an entrySelector panel and the UserForm panel 
 **/

public class UserAdmin extends BaseTabbedAdminPage
{
	@SpringBean
	private	UserService				userService;
	private	ListView				userListView;
	private	final static Logger		logger = Logger.getLogger(UserAdmin.class);
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
							System.err.println("HEREEEEEEEEEE inside UserAdmin ------ populateItem ---- onClick");
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
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object param)
	{
		switch (type)
		{
			case CommonWebUtil.AJAX_ENTRYSELECTOR_FILTER_CHANGE:
			{
				currentFilter = (EntrySelectorFilter)param;
	
				List<User> users = getUsers();
				userListView.setList(users);
				break;
			}
			case CommonWebUtil.AJAX_FORM_SUBMIT:
			case CommonWebUtil.AJAX_DELETE:
			{
				UserBackingBean	backingBean = (UserBackingBean) ((((IWrapModel) param)).getWrappedModel()).getObject();
				
				try
				{
					if (type == CommonWebUtil.AJAX_FORM_SUBMIT)
					{
						persistUser(backingBean);
					}
					else if (type == CommonWebUtil.AJAX_DELETE)
					{
						deleteUser(backingBean);
					}

					// update user list
					List<User> users = getUsers();
					userListView.setList(users);
					
					selectorPanel.refreshList(target);
					
					getTabbedPanel().succesfulSave(target);
				} catch (Exception e)
				{
					e.printStackTrace();
					logger.error("While persisting user", e);
					getTabbedPanel().failedSave(backingBean, target);
				}
				
				break;
			}
		}
	}
	
	/**
	 * Persist user
	 * @param userBackingBean
	 * @throws ObjectNotUniqueException 
	 * @throws PasswordEmptyException 
	 */
	private void persistUser(UserBackingBean userBackingBean) throws PasswordEmptyException, ObjectNotUniqueException
	{
		
		System.err.println(  "  -----> entering persistUser " );
		
		logger.info(((userBackingBean == getTabbedPanel().getEditBackingBean()) 
											? "Updating" 
											: "Adding") + " user :" + userBackingBean.getUser());
		
		if (userBackingBean.isPm())
		{
			logger.debug("Re-adding PM role after edit");
			userBackingBean.getUser().addUserRole(new UserRole(EhourConstants.ROLE_PROJECTMANAGER));
		}
		
		userService.persistUser(userBackingBean.getUser());
	}
	
	/**
	 * 
	 * @param userBackingBean
	 */
	private void deleteUser(UserBackingBean userBackingBean)
	{
		userService.deleteUser(userBackingBean.getUser().getUserId());
	}
	
	/**
	 * Get the users from the backend
	 * @return
	 */
	private List<User> getUsers()
	{
		List<User>	users;
		
		System.err.println(" inside UserAdmin.getUsers ----");
		
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
		
		logger.debug(" leaving inside UserAdmin.getUsers ----");
		
		return users;
	}


	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BasedTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseAddPanel(String panelId)
	{
		return new UserFormPanel(panelId,
				new CompoundPropertyModel(getTabbedPanel().getAddBackingBean()),
				getUserRoles(),
				getUserDepartments());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewAddBackingBean()
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
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewEditBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewEditBaseBackingBean()
	{
		return new UserBackingBean(new User());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BasedTabbedAdminPage#getEditPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseEditPanel(String panelId)
	{
		return new UserFormPanel(panelId,
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
