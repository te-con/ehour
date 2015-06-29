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

package net.rrm.ehour.ui.manage;

import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.entryselector.InactiveFilterChangedEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base admin page template with 2 tabs, add & edit
 */

@SuppressWarnings("serial")
public abstract class AbstractTabbedManagePage<T extends AdminBackingBean> extends AbstractManagePage<T> {
    private AddEditTabbedPanel<T> tabbedPanel;

    public AbstractTabbedManagePage(ResourceModel pageTitle,
                                    ResourceModel addTabTitle,
                                    ResourceModel editTabTitle,
                                    ResourceModel noEntrySelectedText) {
        super(pageTitle);

        tabbedPanel = new AddEditTabbedPanel<T>("tabs", addTabTitle, editTabTitle, noEntrySelectedText) {
            @Override
            protected Panel getAddPanel(String panelId) {
                return getBaseAddPanel(panelId);
            }

            @Override
            protected Panel getEditPanel(String panelId) {
                return getBaseEditPanel(panelId);
            }

            @Override
            protected T createAddBackingBean() {
                return getNewAddBaseBackingBean();
            }

            @Override
            protected T createEditBackingBean() {
                return getNewEditBaseBackingBean();
            }

            @Override
            protected void onTabSwitch(int index) {
                AbstractTabbedManagePage.this.onTabSwitch(index);
            }
        };

        add(tabbedPanel);
    }

    protected void onTabSwitch(int index) {

    }

    /**
     * Get the backing bean for the add panel
     */
    protected abstract T getNewAddBaseBackingBean();

    /**
     * Get the backing bean for the edit panel
     */
    protected abstract T getNewEditBaseBackingBean();

    /**
     * Get the panel for the add tab
     */
    protected abstract Panel getBaseAddPanel(String panelId);

    /**
     * Get the panel for the edit tab
     */
    protected abstract Panel getBaseEditPanel(String panelId);

    /**
     * @return the tabbedPanel
     */
    public final AddEditTabbedPanel<T> getTabbedPanel() {
        return tabbedPanel;
    }

    @Override
    public void onEvent(IEvent<?> event) {
        Object payload = event.getPayload();

        if (payload instanceof InactiveFilterChangedEvent) {
            InactiveFilterChangedEvent inactiveFilterChangedEvent = (InactiveFilterChangedEvent) payload;

            onFilterChanged(inactiveFilterChangedEvent, inactiveFilterChangedEvent.target());
        }
    }

    protected void onFilterChanged(InactiveFilterChangedEvent inactiveFilterChangedEvent, AjaxRequestTarget target) {
    }
}
