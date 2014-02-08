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

import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Selector with autocompletion filter
 */

public class EntrySelectorPanel extends AbstractBasePanel<Void> {
    private static final String WINDOW_ENTRY_SELECTOR_REFRESH = "window.entrySelector.refresh();";
    private IModel<String> checkBoxPrefixText;
    private boolean includeCheckboxToggle = false;
    private GreyBlueRoundedBorder blueBorder;
    private static final long serialVersionUID = -7928428437664050056L;

    private static final JavaScriptResourceReference JS = new JavaScriptResourceReference(EntrySelectorPanel.class, "entrySelector.js");

    public EntrySelectorPanel(String id, WebMarkupContainer itemListHolder) {
        this(id, itemListHolder, null);
    }

    public EntrySelectorPanel(String id, WebMarkupContainer itemListHolder, IModel<String> checkboxPrefix) {
        super(id);

        if (checkboxPrefix != null) {
            this.checkBoxPrefixText = checkboxPrefix;
            includeCheckboxToggle = true;
        }

        setUpPanel(itemListHolder);
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

    private void setUpPanel(WebMarkupContainer itemListHolder) {
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

        selectorFrame.add(getFilterForm());

        add(selectorFrame);

        blueBorder.add(itemListHolder);
    }

    private Form<Void> getFilterForm() {
        final EntrySelectorFilter filter = new EntrySelectorFilter();
        filter.setFilterToggle(getEhourWebSession().getHideInactiveSelections());

        Form<Void> filterForm = new Form<Void>("filterForm");

        WebMarkupContainer filterInputContainer = new WebMarkupContainer("filterInputContainer");
        add(filterInputContainer);
        filterForm.add(filterInputContainer);

        WebMarkupContainer listFilter = new WebMarkupContainer("listFilter");
        listFilter.setMarkupId("listFilter");
        listFilter.setOutputMarkupId(true);
        listFilter.add(AttributeModifier.replace("placeholder", new ResourceModel("report.filter").getObject()));
        filterInputContainer.add(listFilter);


        final AjaxCheckBox deactivateBox = new AjaxCheckBox("filterToggle", new PropertyModel<Boolean>(filter, "filterToggle")) {
            private static final long serialVersionUID = 2585047163449150793L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                getEhourWebSession().setHideInactiveSelections(filter.isFilterToggle());

                callbackAfterFilter(target, filter);

                target.appendJavaScript(WINDOW_ENTRY_SELECTOR_REFRESH);
            }
        };

        deactivateBox.setVisible(includeCheckboxToggle);
        filterForm.add(deactivateBox);

        Label filterToggleText = new Label("filterToggleText", checkBoxPrefixText);
        filterForm.add(filterToggleText);

        return filterForm;
    }

    private void callbackAfterFilter(AjaxRequestTarget target, EntrySelectorFilter filter) {
        send(getPage(), Broadcast.DEPTH, new FilterChangedEvent(filter, target));
    }

    public static class FilterChangedEvent {
        private final EntrySelectorFilter filter;
        private final AjaxRequestTarget target;

        public FilterChangedEvent(EntrySelectorFilter filter, AjaxRequestTarget target) {
            this.filter = filter;
            this.target = target;
        }

        public EntrySelectorFilter getFilter() {
            return filter;
        }

        public void refresh(Component... components) {
            target.add(components);
        }
    }
}
