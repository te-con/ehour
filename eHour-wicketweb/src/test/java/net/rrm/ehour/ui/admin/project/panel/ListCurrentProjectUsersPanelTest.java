package net.rrm.ehour.ui.admin.project.panel;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.DummyUIDataGenerator;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;



public class ListCurrentProjectUsersPanelTest extends AbstractSpringWebAppTester
{
	@SuppressWarnings("serial")
	@Test
	public void shouldRender()
	{
		final List<ProjectAssignment> list = new ArrayList<ProjectAssignment>();
		
		ProjectAssignment projectAssignment = DummyUIDataGenerator.getProjectAssignment(1);
		list.add(projectAssignment);
		
		tester.startPanel(new TestPanelSource()
		{
			public Panel getTestPanel(String panelId)
			{
				return new ListCurrentProjectUsersPanel(panelId, list);
			}
		});
		
		tester.assertNoErrorMessage();
	}
}
