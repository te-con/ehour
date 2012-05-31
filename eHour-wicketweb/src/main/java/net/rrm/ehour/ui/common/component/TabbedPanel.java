package net.rrm.ehour.ui.common.component;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * TODO submit as JIRA issue, isTabVisible -> cache is broken
 * @author thies
 *
 */
public class TabbedPanel extends AjaxTabbedPanel
{
	private static final long serialVersionUID = 589057465612946921L;

	private transient Boolean[] tabsVisibilityCache;

	
	public TabbedPanel(String id, List<ITab> tabs)
	{
		super(id, tabs);
	}

	public void setSelectedTab(int index)
	{
		if (index < 0 || (index >= getTabs().size() && index > 0))
		{
			throw new IndexOutOfBoundsException();
		}

		setDefaultModelObject(index);

		final Component component;

		if (getTabs().size() == 0 || !isTabVisible(index))
		{
			// no tabs or the currently selected tab is not visible
			component = new WebMarkupContainer(TAB_PANEL_ID);
		}
		else
		{
			// show panel from selected tab
			ITab tab = getTabs().get(index);
			component = tab.getPanel(TAB_PANEL_ID);
			if (component == null)
			{
				throw new WicketRuntimeException("ITab.getPanel() returned null. TabbedPanel [" +
					getPath() + "] ITab index [" + index + "]");

			}
		}

		if (!component.getId().equals(TAB_PANEL_ID))
		{
			throw new WicketRuntimeException(
				"ITab.getPanel() returned a panel with invalid id [" +
					component.getId() +
					"]. You must always return a panel with id equal to the provided panelId parameter. TabbedPanel [" +
					getPath() + "] ITab index [" + index + "]");
		}

		addOrReplace(component);
	}
	
	private boolean isTabVisible(int tabIndex)
	{
		if (tabsVisibilityCache == null)
		{
			tabsVisibilityCache = new Boolean[getTabs().size()];
		}

		if (tabsVisibilityCache.length < tabIndex + 1)
		{
			tabsVisibilityCache = Arrays.copyOf(tabsVisibilityCache, tabIndex + 1);		
		}
		
		if (tabsVisibilityCache.length > 0)
		{
			Boolean visible = tabsVisibilityCache[tabIndex];
			
			if (visible == null)
			{
				visible = getTabs().get(tabIndex).isVisible();
				tabsVisibilityCache[tabIndex] = visible;
			}
			
			return visible;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	protected void onDetach()
	{
		tabsVisibilityCache = null;
		super.onDetach();
	}	
}
