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

package net.rrm.ehour.ui.page.admin.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.panel.admin.project.form.ProjectFormPanel;
import net.rrm.ehour.ui.panel.admin.project.form.dto.ProjectAdminBackingBean;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.sort.CustomerComparator;
import net.rrm.ehour.ui.sort.UserComparator;
import net.rrm.ehour.user.domain.User;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Project admin page 
 **/

public class ProjectAdmin  extends BaseTabbedAdminPage
{
	private final String		PROJECT_SELECTOR_ID = "projectSelector";
	private static final long 	serialVersionUID = 9196677804018589806L;
	
	@SpringBean
	private ProjectService		projectService;
	@SpringBean
	private	UserService			userService;
	@SpringBean
	private	CustomerService		customerService;
	private	final static Logger	logger = Logger.getLogger(ProjectAdmin.class);
	private EntrySelectorFilter	currentFilter;
	private	ListView			projectListView;
	private	List<User>			users;
	private	List<Customer>		customers;
	
	/**
	 * 
	 */
	public ProjectAdmin()
	{
		super(new ResourceModel("admin.project.title"),
				new ResourceModel("admin.project.addProject"),
				new ResourceModel("admin.project.editProject"));
		
		List<Project>	projects;
		projects = getProjects();
		
		Fragment projectListHolder = getProjectListHolder(projects);
		
		add(new EntrySelectorPanel(PROJECT_SELECTOR_ID,
				new ResourceModel("admin.project.title"),
				projectListHolder,
				getLocalizer().getString("admin.project.filter", this) + "...",
				getLocalizer().getString("admin.project.hideInactive", this)));		
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getAddPanel(String panelId)
	{
		return new ProjectFormPanel(panelId,
									new CompoundPropertyModel(getAddBackingBean()),
									getUsers(),
									getCustomers());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getEditPanel(java.lang.String)
	 */
	@Override
	protected Panel getEditPanel(String panelId)
	{
		return new ProjectFormPanel(panelId, new CompoundPropertyModel(getEditBackingBean()),
				getUsers(),
				getCustomers());
				
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewAddBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewAddBackingBean()
	{
		Project	project = new Project();
		project.setActive(true);
		
		return new ProjectAdminBackingBean(project);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewEditBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewEditBackingBean()
	{
		return new ProjectAdminBackingBean(new Project());	
	}

	/**
	 * Get a the projectListHolder fragment containing the listView
	 * @param users
	 * @return
	 */
	private Fragment getProjectListHolder(List<Project> projects)
	{
		Fragment fragment = new Fragment("itemListHolder", "itemListHolder", ProjectAdmin.this);
		
		projectListView = new ListView("itemList", projects)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				Project 		project = (Project)item.getModelObject();
				final Integer	projectId = project.getProjectId();
				
				AjaxLink	link = new AjaxLink("itemLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
						setEditBackingBean(new ProjectAdminBackingBean(projectService.getProject(projectId)));
						switchTabOnAjaxTarget(target, 1);
					}
				};
				
				item.add(link);
				// TODO add project count
				link.add(new Label("linkLabel", project.getProjectCode() + " - " + project.getName() + (project.isActive() ? "" : "*"))); 				
			}
		};
		
		fragment.add(projectListView);
		
		return fragment;
	}	
	
	/**
	 * Get the projects from the backend
	 * @return
	 */
	private List<Project> getProjects()
	{
		List<Project> projects;;
		
		if (currentFilter == null)
		{
			projects = projectService.getAllProjects(true);
		}
		else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Filtering on " + currentFilter.getCleanFilterInput() + ", hide active: " + currentFilter.isActivateToggle());
			}
			
			projects = projectService.getProjects(currentFilter.getCleanFilterInput(), currentFilter.isActivateToggle()); 
		}
		
		return projects;
	}
	
	/**
	 * Get users for PM selection
	 * @return
	 */
	private List<User> getUsers()
	{
		if (users == null)
		{
			users = userService.getUsersWithEmailSet();
			
			if (users != null)
			{
				Collections.sort(users, new UserComparator(false));
			}
			else
			{
				users = new ArrayList<User>();
			}
		}
		
		return users;
	}
	
	/**
	 * Get customers for customer dropdown
	 */
	private List<Customer> getCustomers()
	{
		if (customers == null)
		{
			customers = customerService.getCustomers(true);
			
			if (customers != null)
			{
				Collections.sort(customers, new CustomerComparator());
			}
			else
			{
				customers = new ArrayList<Customer>();
			}
		}
		
		return customers;
	}
}
