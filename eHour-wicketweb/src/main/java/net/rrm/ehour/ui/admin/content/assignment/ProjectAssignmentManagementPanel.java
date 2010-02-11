package net.rrm.ehour.ui.admin.content.assignment;

import java.util.Collection;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;

import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created on Feb 10, 2010 9:38:09 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ProjectAssignmentManagementPanel extends AbstractAjaxPanel<Void>
{
	private static final long serialVersionUID = -80524758928247913L;

	@SpringBean(name = "projectAssignmentService")
	private ProjectAssignmentService assignmentService;
	
	public ProjectAssignmentManagementPanel(String id, Collection<ProjectAssignment> assignments)
	{
		super(id);
	}
	
//	private List<ProjectAs>
}
