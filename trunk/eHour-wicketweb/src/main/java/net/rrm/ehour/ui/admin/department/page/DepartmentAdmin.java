/**
 * Created on Aug 20, 2007
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

package net.rrm.ehour.ui.admin.department.page;

import java.util.Collections;
import java.util.List;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.admin.department.common.DepartmentAjaxEventType;
import net.rrm.ehour.ui.admin.department.dto.DepartmentAdminBackingBean;
import net.rrm.ehour.ui.admin.department.panel.DepartmentFormPanel;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.sort.UserDepartmentComparator;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Department admin page
 **/

public class DepartmentAdmin  extends BaseTabbedAdminPage
{
	private final String		DEPT_SELECTOR_ID = "deptSelector";
	private static final long 	serialVersionUID = -6686097898699382233L;

	@SpringBean
	private UserService			userService;
	private	static final Logger	logger = Logger.getLogger(DepartmentAdmin.class);
	private	ListView			deptListView;
	
	/**
	 * Default constructor
	 */
	public DepartmentAdmin()
	{
		super(new ResourceModel("admin.dept.title"),
				new ResourceModel("admin.dept.addDepartment"),
				new ResourceModel("admin.dept.editDepartment"),
				new ResourceModel("admin.dept.noEditEntrySelected"),
				"admin.dept.help.header",
				"admin.dept.help.body");
		
		List<UserDepartment>	departments;
		departments = getUserDepartments();

		Fragment deptListHolder = getDepartmentListHolder(departments);

		GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", 
																new ResourceModel("admin.dept.title"),
																EntrySelectorPanel.ENTRYSELECTOR_WIDTH);
		add(greyBorder);
		
		greyBorder.add(new EntrySelectorPanel(DEPT_SELECTOR_ID, deptListHolder));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BaseTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseAddPanel(String panelId)
	{
		return new DepartmentFormPanel(panelId, new CompoundPropertyModel(getTabbedPanel().getAddBackingBean()));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BaseTabbedAdminPage#getEditPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseEditPanel(String panelId)
	{
		return new DepartmentFormPanel(panelId, new CompoundPropertyModel(getTabbedPanel().getEditBackingBean()));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BaseTabbedAdminPage#getNewAddBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewAddBaseBackingBean()
	{
		return new DepartmentAdminBackingBean(new UserDepartment());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.admin.BaseTabbedAdminPage#getNewEditBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewEditBaseBackingBean()
	{
		return new DepartmentAdminBackingBean(new UserDepartment());
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.page.BasePage#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		AjaxEventType type = ajaxEvent.getEventType();
		
		if (type == DepartmentAjaxEventType.DEPARTMENT_DELETED 
				|| type == DepartmentAjaxEventType.DEPARTMENT_UPDATED)
		{
			// update customer list
			List<UserDepartment> depts = getUserDepartments();
			deptListView.setList(depts);
			
			((EntrySelectorPanel)
					((MarkupContainer)get("entrySelectorFrame"))
						.get(DEPT_SELECTOR_ID)).refreshList(ajaxEvent.getTarget());
			
			getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
		}
		
		return true;
	}	
	
	
	
	
	/**
	 * Get a the departmentListHolder fragment containing the listView
	 * @param users
	 * @return
	 */
	@SuppressWarnings("serial")
	private Fragment getDepartmentListHolder(List<UserDepartment> departments)
	{
		Fragment fragment = new Fragment("itemListHolder", "itemListHolder", DepartmentAdmin.this);
		
		deptListView = new ListView("itemList", departments)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				UserDepartment	dept = (UserDepartment)item.getModelObject();
				final Integer	deptId = dept.getDepartmentId();
				
				AjaxLink	link = new AjaxLink("itemLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
						try
						{
							getTabbedPanel().setEditBackingBean(new DepartmentAdminBackingBean(userService.getUserDepartment(deptId)));
							getTabbedPanel().switchTabOnAjaxTarget(target, 1);

						} catch (ObjectNotFoundException e)
						{
							logger.error(e);
						}
					}
				};
				
				item.add(link);
				link.add(new Label("linkLabel", dept.getCode() + " - " + dept.getName()));				
			}
		};
		
		fragment.add(deptListView);
		
		return fragment;
	}	

	/**
	 * Get the user departments from the backend
	 * @return
	 */
	private List<UserDepartment> getUserDepartments()
	{
		List<UserDepartment>	userDepartments = userService.getUserDepartments();
		Collections.sort(userDepartments, new UserDepartmentComparator());
		
		return userDepartments;
	}	
}
