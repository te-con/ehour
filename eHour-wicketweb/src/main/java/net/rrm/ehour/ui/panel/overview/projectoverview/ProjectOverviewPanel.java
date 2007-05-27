/**
 * Created on May 22, 2007
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

package net.rrm.ehour.ui.panel.overview.projectoverview;

import java.util.ArrayList;
import java.util.Collection;

import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import wicket.Component;
import wicket.markup.html.basic.Label;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;

/**
 * Panel showing overview
 */

public class ProjectOverviewPanel extends Panel
{
	private final Collection<UserProjectStatus> projectStatus;

	private static final long serialVersionUID = -5935376941518756941L;

	/**
	 * 
	 * @param id
	 * @param projectStatus
	 */
	public ProjectOverviewPanel(String id, Collection<UserProjectStatus> projectStatusSet)
	{
		super(id, null);

		this.projectStatus = new ArrayList<UserProjectStatus>(projectStatusSet);
		
		IModel messagesModel = new AbstractReadOnlyModel()
		{
			// Wicket calls this method to get the actual "model object"
			// at runtime
			public Object getObject(Component component)
			{
				return projectStatus;
			}
		};

		ListView view = new ListView("projectStatus", messagesModel)
		{
			public void populateItem(ListItem item)
			{
				UserProjectStatus projectStatus = (UserProjectStatus) item.getModelObject();
				item.add(new Label("projectName", projectStatus.getProjectAssignment().getProject().getName()));
				item.add(new Label("projectCode", projectStatus.getProjectAssignment().getProject().getProjectCode()));
				item.add(new Label("customerName", projectStatus.getProjectAssignment().getProject().getCustomer().getName()));
			}
		};

		add(view);
	}

	/**
	 * @return the projectStatus
	 */
	public Collection<UserProjectStatus> getProjectStatus()
	{
		return projectStatus;
	}
}
