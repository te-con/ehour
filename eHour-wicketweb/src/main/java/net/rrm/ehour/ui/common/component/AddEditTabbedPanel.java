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

package net.rrm.ehour.ui.common.component;

import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.noentry.NoEntrySelectedPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * AjaxTabbedPanel that passes the index to a pre process method
 **/
@SuppressWarnings({"serial"})
public abstract class AddEditTabbedPanel<T extends AdminBackingBean> extends MultiTabbedPanel {
    private static final long serialVersionUID = -2437819961082840272L;

    public static final int TABPOS_ADD = 0;
    public static final int TABPOS_EDIT = 1;

    private T addBackingBean;
    private T editBackingBean;
    private ResourceModel addTabTitle;
    private ResourceModel editTabTitle;
    private ResourceModel noEntrySelectedText;

    public AddEditTabbedPanel(String id,
                              ResourceModel addTabTitle, ResourceModel editTabTitle,
                              ResourceModel noEntrySelectedText) {
        super(id);

        this.addTabTitle = addTabTitle;
        this.editTabTitle = editTabTitle;
        this.noEntrySelectedText = noEntrySelectedText;

        addBackingBean = createAddBackingBean();
        editBackingBean = createEditBackingBean();

        setUpTabs();
    }

    public void forceLoadEditTab(T editBean) {
        editBackingBean = editBean;
        addEditTab();
        setSelectedTab(AddEditTabbedPanel.TABPOS_EDIT);
    }

    /**
     * Successful save
     *
     * @param target
     */
    public void succesfulSave(AjaxRequestTarget target) {
        addBackingBean = createAddBackingBean();
        addBackingBean.setServerMessage(getLocalizer().getString("general.dataSaved", this));
        addAddTab();
        setSelectedTab(TABPOS_ADD);

        target.add(this);
    }


    /**
     * Setup tabs
     */
    private void setUpTabs() {
        addAddTab();
        addNoSelectionTab();
    }

    /**
     * Add add tab at position 0
     */
    private void addAddTab() {
        removeTab(TABPOS_ADD);

        AbstractTab addTab = new AbstractTab(addTabTitle) {
            @Override
            public Panel getPanel(String panelId) {
                return getAddPanel(panelId);
            }
        };

        getTabs().add(TABPOS_ADD, addTab);
    }


    /**
     * Get the panel for the add tab
     *
     * @param panelId
     * @return
     */
    protected abstract Panel getAddPanel(String panelId);

    /**
     * Add no user selected tab at position 1
     */
    private void addNoSelectionTab() {
        removeTab(TABPOS_EDIT);

        AbstractTab editTab = new AbstractTab(editTabTitle) {
            @Override
            public Panel getPanel(String panelId) {
                return getNoSelectionPanel(panelId);
            }
        };

        getTabs().add(TABPOS_EDIT, editTab);
    }

    /**
     * Get the panel for the no-selection-made-yet tab (edit tab but no entity selected yet)
     *
     * @param panelId
     * @return
     */
    private Panel getNoSelectionPanel(String panelId) {
        return new NoEntrySelectedPanel(panelId, false, noEntrySelectedText);
    }


    /**
     * @param target
     * @param index
     */
    @Override
    protected final void preProcessTabSwitch(int index) {
        // if "Add" tab is clicked again, reset the backing bean as it's the
        // only way out if for some reason the save went wrong and the page is stuck on
        // an error
        if (getSelectedTab() == index && index == 0) {
            addBackingBean = createAddBackingBean();
        }

        // reset server messages
        addBackingBean.setServerMessage(null);
        editBackingBean.setServerMessage(null);

        onTabSwitch(index);
    }

    protected void onTabSwitch(int index) {

    }


    /**
     * Switch tab
     *
     * @param tab
     * @param userId
     */
    public void switchTabOnAjaxTarget(AjaxRequestTarget target, int tabIndex) {
        if (tabIndex == TABPOS_EDIT) {
            addEditTab();
        }

        setSelectedTab(tabIndex);
        target.add(this);
    }

    /**
     * Add edit tab at position 1
     */
    private void addEditTab() {
        removeTab(TABPOS_EDIT);

        AbstractTab editTab = new AbstractTab(editTabTitle) {
            @Override
            public Panel getPanel(String panelId) {
                return getEditPanel(panelId);
            }
        };

        getTabs().add(TABPOS_EDIT, editTab);
    }

    /**
     * Get the backing bean for the add panel
     *
     * @return
     */
    protected abstract T createAddBackingBean();


    /**
     * Get the panel for the edit tab
     *
     * @param panelId
     * @return
     */
    protected abstract Panel getEditPanel(String panelId);

    /**
     * @return
     */
    public T getAddBackingBean() {
        return addBackingBean;
    }

    /**
     * @return
     */
    public T getEditBackingBean() {
        return editBackingBean;
    }

    /**
     * Get the backing bean for the edit panel
     *
     * @return
     */
    protected abstract T createEditBackingBean();

    /**
     * @param editBackingBean the editBackingBean to set
     */
    public void setEditBackingBean(T editBackingBean) {
        this.editBackingBean = editBackingBean;
    }
}


