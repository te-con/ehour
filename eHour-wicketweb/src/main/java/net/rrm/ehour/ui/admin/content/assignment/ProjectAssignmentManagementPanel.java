package net.rrm.ehour.ui.admin.content.assignment;

import java.util.List;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.service.project.dto.ProjectAssignmentCollection;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;

/**
 * Created on Feb 10, 2010 9:38:09 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ProjectAssignmentManagementPanel extends AbstractAjaxPanel<Void>
{
	private static final long serialVersionUID = -80524758928247913L;

	
	public ProjectAssignmentManagementPanel(String id, ProjectAssignmentCollection paCollection)
	{
		super(id);
		
		for (List<ProjectAssignment> assignment : paCollection.getAssignments())
		{
			
		}
		
	}
	
//	private List<ProjectAs>
}
