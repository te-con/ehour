package net.rrm.ehour.ui.admin.config.panel;

import net.rrm.ehour.ui.common.component.MultiTabbedPanel;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Config tab panel which acts as a container and manager for the various
 * config tabs
 **/

public class ConfigTabPanel extends MultiTabbedPanel
{
	private static final long serialVersionUID = -8241216529074995434L;

	public ConfigTabPanel(String id)
	{
		super(id);
		
		createTabs();
	}
	
	private void createTabs()
	{
		addTab(ConfigTab.SMTP, new MailServerConfigPanelFactory());
	}
	
	// might want to use reflection.. oh well
	@SuppressWarnings({ "serial", "unchecked" })
	private void addTab(ConfigTab tabDefinition, final TabFactory tabFactory)
	{
		removeTab(tabDefinition.getTabIndex());
		
		AbstractTab tab = new AbstractTab(new ResourceModel(tabDefinition.getTitleResourceId()))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return tabFactory.createTab(panelId);
			}
		};

		getTabs().add(tabDefinition.getTabIndex(), tab);		
	}
	
	
	private class MailServerConfigPanelFactory implements TabFactory
	{
		public Panel createTab(String panelId)
		{
			return new MailServerConfigPanel(panelId);
		}
	}
	
	private interface TabFactory
	{
		Panel createTab(String panelId);
	}
}
