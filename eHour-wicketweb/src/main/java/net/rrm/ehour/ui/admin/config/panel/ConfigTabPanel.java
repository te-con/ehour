package net.rrm.ehour.ui.admin.config.panel;

import java.io.Serializable;

import net.rrm.ehour.ui.common.component.MultiTabbedPanel;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Config tab panel which acts as a container and manager for the various
 * config tabs
 **/

public class ConfigTabPanel extends MultiTabbedPanel
{
	private static final long serialVersionUID = -8241216529074995434L;

	public ConfigTabPanel(String id, IModel model)
	{
		super(id);
		
		createTabs(model);
	}
	
	private void createTabs(IModel model)
	{
		addTab(ConfigTab.MISC, new MiscConfigPanelFactory(), model);
		addTab(ConfigTab.LOCALE, new LocaleConfigPanelFactory(), model);
		addTab(ConfigTab.SMTP, new MailServerConfigPanelFactory(), model);
		addTab(ConfigTab.SKIN, new SkinConfigPanelFactory(), model);
	}
	
	// might want to use reflection.. oh well
	@SuppressWarnings({ "serial", "unchecked" })
	private void addTab(ConfigTab tabDefinition, final TabFactory tabFactory, final IModel model)
	{
		removeTab(tabDefinition.getTabIndex());
		
		AbstractTab tab = new AbstractTab(new ResourceModel(tabDefinition.getTitleResourceId()))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return tabFactory.createTab(panelId, model);
			}
		};

		getTabs().add(tabDefinition.getTabIndex(), tab);		
	}

	@SuppressWarnings("serial")
	private class SkinConfigPanelFactory implements TabFactory
	{
		public Panel createTab(String panelId, IModel model)
		{
			return new SkinConfigPanel(panelId, model);
		}
	}
	
	
	@SuppressWarnings("serial")
	private class MiscConfigPanelFactory implements TabFactory
	{
		public Panel createTab(String panelId, IModel model)
		{
			return new MiscConfigPanel(panelId, model);
		}
	}
	
	@SuppressWarnings("serial")
	private class LocaleConfigPanelFactory implements TabFactory
	{
		public Panel createTab(String panelId, IModel model)
		{
			return new LocaleConfigPanel(panelId, model);
		}
	}
	
	@SuppressWarnings("serial")
	private class MailServerConfigPanelFactory implements TabFactory
	{
		public Panel createTab(String panelId, IModel model)
		{
			return new MailServerConfigPanel(panelId, model);
		}
	}
	
	private interface TabFactory extends Serializable
	{
		Panel createTab(String panelId, IModel model);
	}
}
