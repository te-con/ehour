package net.rrm.ehour.ui.admin.content.assignment;

import java.util.Collection;
import java.util.List;

import net.rrm.ehour.domain.MotherUtil;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentMother;
import net.rrm.ehour.domain.ProjectMother;
import net.rrm.ehour.domain.UserMother;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;


/**
 * Created on Feb 10, 2010 9:38:31 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ProjectAssignmentManagementPanelTest extends AbstractSpringWebAppTester
{
	@Test
	public void render()
	{
		ProjectAssignment assignment = ProjectAssignmentMother.createProjectAssignment(UserMother.createUser(), ProjectMother.createProject(1));
		List<ProjectAssignment> assignments = MotherUtil.createMultiple(assignment);
		
		startPanel(assignments);
		tester.assertNoErrorMessage();
	}
	
	@SuppressWarnings("serial")
	private void startPanel(final Collection<ProjectAssignment> assignments)
	{
		tester.startPanel(new TestPanelSource()
		{
			
			@Override
			public Panel getTestPanel(String panelId)
			{
				return new ProjectAssignmentManagementPanel(panelId, assignments);
			}
		});
	}
}
