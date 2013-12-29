/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.report.panel.criteria;

import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;

import java.util.List;

/**
 * Ajax tabbed report panel
 */

public class ReportTabbedPanel extends AjaxTabbedPanel<ITab> {
    private static final long serialVersionUID = 5957279200970383021L;

    public ReportTabbedPanel(final String id, final List<ITab> tabs) {
        super(id, tabs);

        setOutputMarkupId(true);
    }

    @SuppressWarnings("serial")
    @Override
    protected WebMarkupContainer newLink(final String linkId, final int index) {
        return new AjaxFallbackLink<String>(linkId) {
            public void onClick(final AjaxRequestTarget target) {
                setSelectedTab(index);
                if (target != null) {
                    target.add(ReportTabbedPanel.this);
                }
                onAjaxUpdate(target);
            }

            // only diff with overriden method
            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
            }
        };
    }

    /**
     * Add tab, replacing any tabs that have the same title resource key
     *
     * @param newTab make sure to use a KeyResourceModel for the title
     */
    public void addTab(final ITab newTab) {
        final List<ITab> tabList = getTabs();
        boolean tabNotAdded = true;

        final String key = (newTab.getTitle() instanceof KeyResourceModel) ? ((KeyResourceModel) newTab.getTitle()).getKey() : "";

        int tabIndex = 0;

        for (final ITab tab : tabList) {
            if (tab.getTitle() instanceof KeyResourceModel) {
                if (((KeyResourceModel) tab.getTitle()).getKey().equals(key)) {
                    tabList.set(tabIndex, newTab);
                    this.setSelectedTab(tabIndex);

                    tabNotAdded = false;
                    break;
                }
            }

            tabIndex++;
        }

        if (tabNotAdded) {
            getTabs().add(newTab);
            setSelectedTab(tabList.size() - 1);
        }
    }
}
