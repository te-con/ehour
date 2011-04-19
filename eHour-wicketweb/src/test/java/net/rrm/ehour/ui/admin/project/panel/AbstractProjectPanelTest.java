package net.rrm.ehour.ui.admin.project.panel;

import java.util.ArrayList;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.DummyUIDataGenerator;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.junit.Before;

public abstract class AbstractProjectPanelTest extends AbstractSpringWebAppTester
{
	protected ArrayList<ProjectAssignment> assignments;

	@Before
	public final void setup()
	{
		assignments = new ArrayList<ProjectAssignment>();
		
		ProjectAssignment projectAssignment = DummyUIDataGenerator.getProjectAssignment(1);
		assignments.add(projectAssignment);
	}
}
