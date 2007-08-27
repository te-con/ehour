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

import java.util.Collections;
import java.util.List;

import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.sort.ProjectAssignmentComparator;
import net.rrm.ehour.user.domain.User;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * List of existing assignments 
 **/

public class AssignmentListPanel extends Panel
{
	private static final long serialVersionUID = -8798859357268916546L;

	@SpringBean
	private ProjectService	projectService;
	
	/**
	 * 
	 * @param id
	 */
	public AssignmentListPanel(String id, User user)
	{
		super(id);
		
		List<ProjectAssignment >assignments = projectService.getAllProjectsForUser(user.getUserId());
		
		if (assignments != null && assignments.size() > 0)
		{
			Collections.sort(assignments, new ProjectAssignmentComparator(ProjectAssignmentComparator.ASSIGNMENT_COMPARE_CUSTDATEPRJ));
		}
	
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("border");
		add(greyBorder);

	}

}
