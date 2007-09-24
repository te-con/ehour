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

package net.rrm.ehour.ui.page.admin.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.panel.admin.project.form.ProjectFormPanel;
import net.rrm.ehour.ui.panel.admin.project.form.dto.ProjectAdminBackingBean;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.sort.CustomerComparator;
import net.rrm.ehour.ui.sort.UserComparator;
import net.rrm.ehour.ui.util.CommonUIStaticData;
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
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
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
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", 
											new ResourceModel("admin.project.title"), 
											EntrySelectorPanel.ENTRYSELECTOR_WIDTH);
		add(greyBorder);		
		
		greyBorder.add(new EntrySelectorPanel(PROJECT_SELECTOR_ID,
											projectListHolder,
											new StringResourceModel("admin.project.filter", this, null),
											new ResourceModel("admin.project.hideInactive")));		
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
			case CommonUIStaticData.AJAX_ENTRYSELECTOR_FILTER_CHANGE:
			{
				currentFilter = (EntrySelectorFilter)param;
	
				List<Project> projects = getProjects();
				projectListView.setList(projects);
				break;
			}
			case CommonUIStaticData.AJAX_DELETE:
			case CommonUIStaticData.AJAX_FORM_SUBMIT:
			{
				ProjectAdminBackingBean backingBean = (ProjectAdminBackingBean) ((((IWrapModel) param)).getWrappedModel()).getObject();
				try
				{
					if (type == CommonUIStaticData.AJAX_FORM_SUBMIT)
					{
						persistProject(backingBean);
					}
					else if (type == CommonUIStaticData.AJAX_DELETE)
					{
						deleteProject(backingBean);
					}					

					// update project list
					List<Project> projects = getProjects();
					projectListView.setList(projects);
					
					((EntrySelectorPanel)get(PROJECT_SELECTOR_ID)).refreshList(target);
					
					getTabbedPanel().succesfulSave(target);
				} catch (Exception e)
				{
					logger.error("While persisting/deleting project", e);
					getTabbedPanel().failedSave(backingBean, target);
				}
				
				break;
			}
		}
	}
	
	/**
	 * Persist project
	 */
	private void persistProject(ProjectAdminBackingBean backingBean)
	{
		projectService.persistProject(backingBean.getProject());
	}
	
	/**
	 * Delete project
	 * @throws ParentChildConstraintException 
	 */
	private void deleteProject(ProjectAdminBackingBean backingBean) throws ParentChildConstraintException
	{
		projectService.deleteProject(backingBean.getProject().getProjectId());
	} 

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseAddPanel(String panelId)
	{
		return new ProjectFormPanel(panelId,
									new CompoundPropertyModel(getTabbedPanel().getAddBackingBean()),
									getUsers(),
									getCustomers());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getEditPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseEditPanel(String panelId)
	{
		return new ProjectFormPanel(panelId, new CompoundPropertyModel(getTabbedPanel().getEditBackingBean()),
				getUsers(),
				getCustomers());
				
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewAddBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewAddBaseBackingBean()
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
	protected AdminBackingBean getNewEditBaseBackingBean()
	{
		return new ProjectAdminBackingBean(new Project());	
	}

	/**
	 * Get a the projectListHolder fragment containing the listView
	 * @param users
	 * @return
	 */
	@SuppressWarnings("serial")
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
						try
						{
							getTabbedPanel().setEditBackingBean(new ProjectAdminBackingBean(projectService.getProjectAndCheckDeletability(projectId)));
							getTabbedPanel().switchTabOnAjaxTarget(target, 1);
						} catch (ObjectNotFoundException e)
						{
							logger.error(e);
						}
					}
				};
				
				item.add(link);

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
