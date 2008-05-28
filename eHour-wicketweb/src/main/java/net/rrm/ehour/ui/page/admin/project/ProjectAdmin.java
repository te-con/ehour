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

import java.util.List;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.ajax.AjaxEvent;
import net.rrm.ehour.ui.ajax.PayloadAjaxEvent;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage;
import net.rrm.ehour.ui.panel.admin.customer.CustomerAjaxEventType;
import net.rrm.ehour.ui.panel.admin.customer.form.CustomerFormPanel;
import net.rrm.ehour.ui.panel.admin.customer.form.dto.CustomerAdminBackingBean;
import net.rrm.ehour.ui.panel.admin.project.form.ProjectFormPanel;
import net.rrm.ehour.ui.panel.admin.project.form.dto.ProjectAdminBackingBeanImpl;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.util.CommonWebUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
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
 * Project admin page 
 **/

public class ProjectAdmin  extends BaseTabbedAdminPage
{
	private static final int	TABPOS_NEW_CUSTOMER  = 2;
	private static final String	PROJECT_SELECTOR_ID = "projectSelector";
	private static final long 	serialVersionUID = 9196677804018589806L;
	
	@SpringBean
	private ProjectService		projectService;
	private	final static Logger	logger = Logger.getLogger(ProjectAdmin.class);
	private EntrySelectorFilter	currentFilter;
	private	ListView			projectListView;
	
	/**
	 * 
	 */
	public ProjectAdmin()
	{
		super(new ResourceModel("admin.project.title"),
				new ResourceModel("admin.project.addProject"),
				new ResourceModel("admin.project.editProject"),
				new ResourceModel("admin.project.noEditEntrySelected"),
				"admin.project.help.header",
				"admin.project.help.body");
		
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
			case CommonWebUtil.AJAX_ENTRYSELECTOR_FILTER_CHANGE:
			{
				currentFilter = (EntrySelectorFilter)param;
	
				List<Project> projects = getProjects();
				projectListView.setList(projects);
				break;
			}
			case CommonWebUtil.AJAX_FORM_SUBMIT:
			{
				// update project list
				List<Project> projects = getProjects();
				projectListView.setList(projects);
				
				((EntrySelectorPanel)
						((MarkupContainer)get("entrySelectorFrame"))
							.get(PROJECT_SELECTOR_ID)).refreshList(target);
				
				getTabbedPanel().succesfulSave(target);
				break;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.BasePage#ajaxEventReceived(net.rrm.ehour.ui.ajax.AjaxEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean ajaxEventReceived(AjaxEvent event)
	{
		if (event.getEventType() == CustomerAjaxEventType.ADMIN_CUSTOMER_NEW_PANEL_REQUEST)
		{
			addNewCustomerTab(event.getTarget());
			
			return false;
		}
		else if (event.getEventType() == CustomerAjaxEventType.ADMIN_CUSTOMER_UPDATED)
		{
			PayloadAjaxEvent<Customer> payloadEvent = (PayloadAjaxEvent<Customer>)event;
			newCustomerAdded(payloadEvent.getTarget(), payloadEvent.getPayload());
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param target
	 * @param customer
	 */
	private void newCustomerAdded(AjaxRequestTarget target, Customer customer)
	{
		// first remove the tab
		getTabbedPanel().removeTab(TABPOS_NEW_CUSTOMER);
		target.addComponent(getTabbedPanel());
	}
	
	/**
	 * Add new customer tab
	 * @param target
	 */
	private void addNewCustomerTab(AjaxRequestTarget target)
	{
		AbstractTab tab = new AbstractTab(new ResourceModel("admin.customer.addCustomer"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new CustomerFormPanel(panelId, new CompoundPropertyModel(CustomerAdminBackingBean.createCustomerAdminBackingBean()));
			}
		};
		
		getTabbedPanel().addTab(tab, TABPOS_NEW_CUSTOMER);
		getTabbedPanel().setSelectedTab(TABPOS_NEW_CUSTOMER);
		
		target.addComponent(getTabbedPanel());
	}


	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getAddPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseAddPanel(String panelId)
	{
		return new ProjectFormPanel(panelId,
									new CompoundPropertyModel(getTabbedPanel().getAddBackingBean()));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getEditPanel(java.lang.String)
	 */
	@Override
	protected Panel getBaseEditPanel(String panelId)
	{
		return new ProjectFormPanel(panelId, new CompoundPropertyModel(getTabbedPanel().getEditBackingBean()));
				
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
		
		return new ProjectAdminBackingBeanImpl(project);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.admin.BaseTabbedAdminPage#getNewEditBackingBean()
	 */
	@Override
	protected AdminBackingBean getNewEditBaseBackingBean()
	{
		return new ProjectAdminBackingBeanImpl(new Project());	
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
							getTabbedPanel().setEditBackingBean(new ProjectAdminBackingBeanImpl(projectService.getProjectAndCheckDeletability(projectId)));
							getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
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
}
