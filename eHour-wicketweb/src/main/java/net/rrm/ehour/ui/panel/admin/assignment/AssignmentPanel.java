/**
 * Created on Aug 23, 2007
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

package net.rrm.ehour.ui.panel.admin.assignment;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.ui.component.CustomAjaxTabbedPanel;
import net.rrm.ehour.ui.model.AdminBackingBean;
import net.rrm.ehour.ui.panel.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.panel.admin.user.form.dto.UserBackingBean;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Assignment panel displaying the list 
 **/

public class AssignmentPanel extends Panel
{
	private static final long serialVersionUID = -3721224427697057895L;
	
	private CustomAjaxTabbedPanel	tabbedPanel;
	private List<Customer> 			customers;
	private List<ProjectAssignmentType> types;
	
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public AssignmentPanel(String id,
							IModel model,
							List<Customer> customers,
							List<ProjectAssignmentType> types)
	{
		super(id, model);

		this.customers = customers;
		this.types = types;
		
		AssignmentListPanel	listPanel = new AssignmentListPanel("assignmentList");
		add(listPanel);
		
		setUpTabs();
		
		add(tabbedPanel);
	}
	
	/**
	 * Tabbed panels
	 */
	private void setUpTabs()
	{
		List<AbstractTab>	tabs = new ArrayList<AbstractTab>(2);
		
		tabbedPanel = new CustomAjaxTabbedPanel("tabs", tabs)
		{
//			@Override
//			protected void preProcessTabSwitch(int index)
//			{
//				// if "Add user" is clicked again, reset the backing bean as it's
//				// only way out if for some reason the save went wrong and the page is stuck on
//				// an error
//				if (getSelectedTab() == index && index == 0)
//				{
//					addBackingBean = getNewAddBackingBean();
//				}
//				
//				// reset server messages
//				addBackingBean.setServerMessage(null);
//				editBackingBean.setServerMessage(null);
//			}
		};
		
		addAddTab();
	}
	
	
	/**
	 * Add add tab at position 0
	 */
	@SuppressWarnings("unchecked")
	private void addAddTab()
	{
		tabbedPanel.removeTab(0);
		
		AbstractTab addTab = new AbstractTab(new ResourceModel("admin.assignment.newAssignment"))
		{
			private static final long serialVersionUID = 6208220479189701348L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new AssignmentFormPanel(panelId,
												new CompoundPropertyModel(getNewAddBackingBean()),
												customers,
												types);
			}
		};

		tabbedPanel.getTabs().add(0, addTab);	
	}		

	/**
	 * Get new add backing bean
	 * @return
	 */
	private AdminBackingBean getNewAddBackingBean()
	{
		AssignmentAdminBackingBean	assignmentBean;
		
		assignmentBean = new AssignmentAdminBackingBean(new ProjectAssignment());
		assignmentBean.getProjectAssignment().setActive(true);

		return assignmentBean;
	}	
	
	/**
	 * get new edit bean
	 * @return
	 */
	private AdminBackingBean getNewEditBackingBean()
	{
		return new AssignmentAdminBackingBean(new ProjectAssignment());
	}	
	
}
