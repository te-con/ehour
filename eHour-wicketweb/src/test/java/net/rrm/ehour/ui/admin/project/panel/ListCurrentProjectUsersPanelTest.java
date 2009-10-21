package net.rrm.ehour.ui.admin.project.panel;

import net.rrm.ehour.ui.admin.project.panel.editusers.ListCurrentProjectUsersPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;



public class ListCurrentProjectUsersPanelTest extends AbstractProjectPanelTest
{
	@SuppressWarnings("serial")
	@Test
	public void shouldRender()
	{
		tester.startPanel(new TestPanelSource()
		{
			public Panel getTestPanel(String panelId)
			{
				return new ListCurrentProjectUsersPanel(panelId, assignments);
			}
		});
		
		tester.assertNoErrorMessage();
	}
}
