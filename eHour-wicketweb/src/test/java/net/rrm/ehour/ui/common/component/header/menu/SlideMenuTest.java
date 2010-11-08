package net.rrm.ehour.ui.common.component.header.menu;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.common.component.header.menu.MenuItem;
import net.rrm.ehour.ui.common.component.header.menu.SlideMenu;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Test;


public class SlideMenuTest extends AbstractSpringWebAppTester
{
	@Test
	public void shouldRender()
	{
		List<MenuItem> items = new ArrayList<MenuItem>();
		
		items.add(new MenuItem("blabla"));
		
		startPanel(items);
		
		tester.assertNoErrorMessage();
	}
	
	@SuppressWarnings("serial")
	private void startPanel(final List<MenuItem> items)
	{
		tester.startPanel(new TestPanelSource()
		{
			
			public Panel getTestPanel(String panelId)
			{
				return new SlideMenu(panelId, items);
			}
		});
	}
}
