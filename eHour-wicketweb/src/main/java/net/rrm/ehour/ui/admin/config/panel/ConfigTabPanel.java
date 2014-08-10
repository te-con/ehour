package net.rrm.ehour.ui.admin.config.panel;

import net.rrm.ehour.ui.admin.config.MainConfigBackingBean;
import net.rrm.ehour.ui.common.component.MultiTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.io.Serializable;

/**
 * Config tab panel which acts as a container and manager for the various
 * config tabs
 */

public class ConfigTabPanel extends MultiTabbedPanel {
    private static final long serialVersionUID = -8241216529074995434L;

    public ConfigTabPanel(String id, IModel<MainConfigBackingBean> model) {
        super(id);

        createTabs(model);
    }

    private void createTabs(IModel<MainConfigBackingBean> model) {
        addTab(ConfigTab.MISC, new MiscConfigPanelFactory(), model);
        addTab(ConfigTab.LOCALE, new LocaleConfigPanelFactory(), model);
        addTab(ConfigTab.SKIN, new SkinConfigPanelFactory(), model);
        addTab(ConfigTab.AUDIT, new AuditConfigPanelFactory(), model);
    }

    @SuppressWarnings({"serial"})
    private void addTab(ConfigTab tabDefinition, final TabFactory tabFactory, final IModel<MainConfigBackingBean> model) {
        removeTab(tabDefinition.getTabIndex());

        AbstractTab tab = new AbstractTab(new ResourceModel(tabDefinition.getTitleResourceId())) {
            @Override
            public Panel getPanel(String panelId) {
                return tabFactory.createTab(panelId, model);
            }
        };

        getTabs().add(tabDefinition.getTabIndex(), tab);
    }

    @SuppressWarnings("serial")
    private static class AuditConfigPanelFactory implements TabFactory {
        public Panel createTab(String panelId, IModel<MainConfigBackingBean> model) {
            return new AuditConfigPanel(panelId, model);
        }
    }

    @SuppressWarnings("serial")
    private static class SkinConfigPanelFactory implements TabFactory {
        public Panel createTab(String panelId, IModel<MainConfigBackingBean> model) {
            return new SkinConfigPanel(panelId, model);
        }
    }


    @SuppressWarnings("serial")
    private static class MiscConfigPanelFactory implements TabFactory {
        public Panel createTab(String panelId, IModel<MainConfigBackingBean> model) {
            return new MiscConfigPanel(panelId, model);
        }
    }

    @SuppressWarnings("serial")
    private static class LocaleConfigPanelFactory implements TabFactory {
        public Panel createTab(String panelId, IModel<MainConfigBackingBean> model) {
            return new LocaleConfigPanel(panelId, model);
        }
    }

    private static interface TabFactory extends Serializable {
        Panel createTab(String panelId, IModel<MainConfigBackingBean> model);
    }
}
