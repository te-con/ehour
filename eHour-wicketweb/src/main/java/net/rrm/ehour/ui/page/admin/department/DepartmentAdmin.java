/**
 * Created on Aug 20, 2007
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

package net.rrm.ehour.ui.page.admin.department;

import java.util.Collections;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.panel.admin.customer.form.dto.CustomerAdminBackingBean;
import net.rrm.ehour.ui.panel.admin.department.form.DepartmentFormPanel;
import net.rrm.ehour.ui.panel.admin.department.form.dto.DepartmentAdminBackingBean;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.sort.UserDepartmentComparator;
import net.rrm.ehour.ui.util.CommonStaticData;
import net.rrm.ehour.user.domain.UserDepartment;
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
import org.apache.wicket.model.IWrapModel;
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
				new ResourceModel("admin.dept.editDepartment"));
		
		List<UserDepartment>	departments;
		departments = getUserDepartments();

		Fragment deptListHolder = getDepartmentListHolder(departments);

		add(new EntrySelectorPanel(DEPT_SELECTOR_ID,
				new ResourceModel("admin.dept.title"),
				deptListHolder));		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getAddPanel(String panelId)
	{
		return new DepartmentFormPanel(panelId, new CompoundPropertyModel(getAddBackingBean()));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getEditPanel(java.lang.String)
	 */
	@Override
	protected Panel getEditPanel(String panelId)
	{
		return new DepartmentFormPanel(panelId, new CompoundPropertyModel(getEditBackingBean()));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewAddBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewAddBackingBean()
	{
		return new DepartmentAdminBackingBean(new UserDepartment());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewEditBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewEditBackingBean()
	{
		return new DepartmentAdminBackingBean(new UserDepartment());
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
			case CommonStaticData.AJAX_FORM_SUBMIT:
			{
				DepartmentAdminBackingBean backingBean = (DepartmentAdminBackingBean) ((((IWrapModel) param)).getWrappedModel()).getObject();
				try
				{
					persistDepartment(backingBean);

					// update customer list
					List<UserDepartment> depts = getUserDepartments();
					deptListView.setList(depts);
					
					((EntrySelectorPanel)get(DEPT_SELECTOR_ID)).refreshList(target);
					
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
	 * Persist dept
	 * @param backingBean
	 * @throws ObjectNotUniqueException 
	 */
	private void persistDepartment(DepartmentAdminBackingBean backingBean) throws ObjectNotUniqueException
	{
		userService.persistUserDepartment(backingBean.getDepartment());
	}
	
	/**
	 * Get a the departmentListHolder fragment containing the listView
	 * @param users
	 * @return
	 */
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
						setEditBackingBean(new DepartmentAdminBackingBean(userService.getUserDepartment(deptId)));
						switchTabOnAjaxTarget(target, 1);
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
