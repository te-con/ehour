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

package net.rrm.ehour.ui.common.panel.entryselector;

import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.io.Serializable;

import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData.EntrySelectorRow;

/**
 * Selector with autocompletion filter
 */

public class EntrySelectorPanel<T> extends AbstractBasePanel<T> {
    public static final String ITEM_LIST_HOLDER_ID = "itemListHolder";

    private static final String WINDOW_ENTRY_SELECTOR_REFRESH = "window.entrySelector.refresh();";
    private static final JavaScriptResourceReference JS = new JavaScriptResourceReference(EntrySelectorPanel.class, "entrySelector.js");

    private IModel<String> hideInactiveCheckboxPrefix;
    private boolean showHideInactiveCheckbox = false;
    private GreyBlueRoundedBorder blueBorder;


    public <I> EntrySelectorPanel(String id, EntrySelectorData<I> entrySelectorData) {
        this(id, entrySelectorData, null);
    }

    public <I> EntrySelectorPanel(String id, EntrySelectorData<I> entrySelectorData, IModel<String> checkboxPrefix) {
        super(id);

        if (checkboxPrefix != null) {
            this.hideInactiveCheckboxPrefix = checkboxPrefix;
            showHideInactiveCheckbox = true;
        }

        setUpPanel(entrySelectorData);
    }

    @Override
    public void onEvent(IEvent<?> eventWrapper) {
        Object event = eventWrapper.getPayload();

        if (event instanceof EntryListUpdatedEvent) {
            EntryListUpdatedEvent entryListUpdatedEvent = (EntryListUpdatedEvent) event;

            refreshList(entryListUpdatedEvent.target());
        }
    }

    public void refreshList(AjaxRequestTarget target) {
        target.add(blueBorder);
        target.appendJavaScript(WINDOW_ENTRY_SELECTOR_REFRESH);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(JS));
        response.render(OnDomReadyHeaderItem.forScript("window.entrySelector = new EntrySelector('#listFilter', '.entrySelectorTable');"));
    }

    private <I> void setUpPanel(EntrySelectorData<I> entrySelectorData) {
        WebMarkupContainer selectorFrame = new WebMarkupContainer("entrySelectorFrame");

        blueBorder = new GreyBlueRoundedBorder("blueBorder") {
            @Override
            protected WebMarkupContainer createComponent() {
                WebMarkupContainer component = super.createComponent();

                component.add(AttributeModifier.append("class", "inline"));

                return component;
            }
        };

        blueBorder.setOutputMarkupId(true);
        selectorFrame.add(blueBorder);

        selectorFrame.add(createForm());

        add(selectorFrame);

        blueBorder.add(createListView("itemList", entrySelectorData));
    }

    private <I> EntrySelectorListView createListView(String id, EntrySelectorData<I> entrySelectorData) {
        return new EntrySelectorListView(id, entrySelectorData.getRows(), null) {

            @Override
            protected void onPopulate(ListItem<EntrySelectorRow<I>> item, IModel<EntrySelectorRow<I>> itemModel) {
                EntrySelectorRow object = item.getModelObject();

                RepeatingView cells = new RepeatingView("child");

                for (Serializable serializable : object.getCells()) {
                    cells.add(new Label(cells.newChildId(), serializable));
                }

                item.add(cells);
            }
        };
    }


    private Form<Void> createForm() {
        Form<Void> form = new Form<Void>("filterForm");

        WebMarkupContainer filterInputContainer = new WebMarkupContainer("filterInputContainer");
        add(filterInputContainer);
        form.add(filterInputContainer);

        WebMarkupContainer listFilter = new WebMarkupContainer("listFilter");
        listFilter.setMarkupId("listFilter");
        listFilter.setOutputMarkupId(true);
        listFilter.add(AttributeModifier.replace("placeholder", new ResourceModel("report.filter").getObject()));
        filterInputContainer.add(listFilter);

        final HideInactiveFilter hideInactiveFilter = new HideInactiveFilter();
        hideInactiveFilter.setHideInactive(getEhourWebSession().getHideInactiveSelections());
        final AjaxCheckBox hideInactiveCheckbox = new AjaxCheckBox("filterToggle", new PropertyModel<Boolean>(hideInactiveFilter, "hideInactive")) {
            private static final long serialVersionUID = 2585047163449150793L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                getEhourWebSession().setHideInactiveSelections(hideInactiveFilter.isHideInactive());

                send(getPage(), Broadcast.DEPTH, new InactiveFilterChangedEvent(hideInactiveFilter, target));

                target.appendJavaScript(WINDOW_ENTRY_SELECTOR_REFRESH);
            }
        };

        hideInactiveCheckbox.setVisible(showHideInactiveCheckbox);
        form.add(hideInactiveCheckbox);

        Label filterToggleText = new Label("filterToggleText", hideInactiveCheckboxPrefix);
        form.add(filterToggleText);

        return form;
    }

    private static final long serialVersionUID = -7928428437664050056L;

    public interface ClickHandler {
        void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException;
    }
}

