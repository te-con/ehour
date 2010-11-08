package net.rrm.ehour.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO once this junit test fails remove the AbstractTabbedPanel class
 *
 * With wicket extensions 1.4.3, TabbedPanel throws an IndexOutOfBounds exception on the tabsVisibilityCache array when adding more tabs after the panel is rendered.
 *
 * The onBeforeRender method calls the isTabVisible method which initializes an array of booleans for visibility caching. The length of this array is the amount of tabs added at that time. When - through ajax - a new tab is added after rendering and setSelectedTab is called an exception is thrown at:
 *  Boolean visible = tabsVisibilityCache[tabIndex];
 *
 * Fix: increase the array before checking or use a List:
 * 
 *     if (tabsVisibilityCache.length < tabIndex + 1)
 *     {
 *         tabsVisibilityCache = Arrays.copyOf(tabsVisibilityCache, tabIndex + 1);
 *     }
 *     
 *     	private boolean isTabVisible(int tabIndex)
 *		{
 *			if (tabsVisibilityCache == null)
 *			{
 *				tabsVisibilityCache = new Boolean[tabs.size()];
 *			}
 *
 *			if (tabsVisibilityCache.length < tabIndex + 1)
 *			{
 *				tabsVisibilityCache = Arrays.copyOf(tabsVisibilityCache, tabIndex + 1);		
 *			}
 *	
 *			if (tabsVisibilityCache.length > 0)
 *			{
 *				Boolean visible = tabsVisibilityCache[tabIndex];
 *				if (visible == null)
 *				{
 *					visible = tabs.get(tabIndex).isVisible();
 *					tabsVisibilityCache[tabIndex] = visible;
 *				}
 *				return visible;
 *			}
 *			else
 *			{
 *				return false;
 *			}
 *		}
 *        
 * @author Thies Edeling (thies@te-con.nl http://www.te-con.n/)
 *
 */
@SuppressWarnings("serial")
public class TabbedPanelTest
{
	private WicketTester tester;
	private TabbedPanel panel;
	
	/**
	 * Selecting tab after modification is okay
	 */
	@Test
	public void selectTabAfterModification()
	{
		panel.getTabs().add(createTab("title"));
		panel.setSelectedTab(1);
	}

	/**
	 * Selecting before modification throws an exception
	 */
	@Test(expected=IndexOutOfBoundsException.class)
	public void selectTabBeforeModification()
	{
		panel.setSelectedTab(1);
		panel.getTabs().add(createTab("title"));
	}
	
	@After
	public void assertAfter()
	{
		tester.assertNoErrorMessage();

	}
	
	@Before
	public void initPanels()
	{
		tester = new WicketTester();
		
		startPanel(tester);

		panel = (TabbedPanel)tester.getComponentFromLastRenderedPage("panel");
	}
	
	private void startPanel(WicketTester tester)
	{
		tester.startPanel(new TestPanelSource()
		{
			public Panel getTestPanel(String panelId)
			{
				return createPanel(panelId);
			}
		});
	}
	
	private TabbedPanel createPanel(String id)
	{
		return new TabbedPanel(id, createTabs(1));
	}
	
	private List<ITab> createTabs(int amount)
	{
		List<ITab> tabs = new ArrayList<ITab>();
		
		for (int i = 0; i < amount; i++)
		{
			tabs.add(createTab(Integer.toString(i)));
		}
		
		return tabs;
	}

	private AbstractTab createTab(String title)
	{
		return new AbstractTab(new Model<String>(title))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new EmptyPanel(panelId);
			}
		};
	}
}
