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
import net.rrm.ehour.ui.common.component.XlsxLink;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.report.excel.ExcelWorkbook;
import net.rrm.ehour.ui.common.report.excel.IWriteBytes;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData.EntrySelectorRow;

/**
 * Selector with autocompletion filter
 */

public class EntrySelectorPanel extends AbstractBasePanel<EntrySelectorData> {
    private static final String WINDOW_ENTRY_SELECTOR_REFRESH = "window.entrySelector.refresh();";
    private static final JavaScriptResourceReference JS = new JavaScriptResourceReference(EntrySelectorPanel.class, "entrySelector.js");
    private static final String ITEM_LIST_ID = "itemList";
    private static final String HEADER_ID = "headers";
    private final ClickHandler clickHandler;

    private IModel<String> hideInactiveCheckboxTooltip;
    private boolean showHideInactiveLink = false;
    private GreyBlueRoundedBorder blueBorder;


    public EntrySelectorPanel(String id, EntrySelectorData entrySelectorData, ClickHandler clickHandler) {
        this(id, entrySelectorData, clickHandler, null);
    }

    public EntrySelectorPanel(String id, EntrySelectorData entrySelectorData, ClickHandler clickHandler, IModel<String> checkboxTooltip) {
        super(id, new Model<EntrySelectorData>(entrySelectorData));
        this.clickHandler = clickHandler;

        if (checkboxTooltip != null) {
            this.hideInactiveCheckboxTooltip = checkboxTooltip;
            showHideInactiveLink = true;
        }

        setUpPanel();
    }

    @Override
    public void onEvent(IEvent<?> eventWrapper) {
        Object event = eventWrapper.getPayload();

        if (event instanceof EntryListUpdatedEvent) {
            EntryListUpdatedEvent entryListUpdatedEvent = (EntryListUpdatedEvent) event;

            reRender(entryListUpdatedEvent.target());
        }
    }

    public void updateData(EntrySelectorData entrySelectorData) {
        setDefaultModelObject(entrySelectorData);

        blueBorder.addOrReplace(createListView(ITEM_LIST_ID));
    }

    public void reRender(AjaxRequestTarget target) {
        target.add(blueBorder);
        target.appendJavaScript(WINDOW_ENTRY_SELECTOR_REFRESH);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(JS));
        response.render(OnDomReadyHeaderItem.forScript("window.entrySelector = new EntrySelector('#listFilter', '.entrySelectorTable');"));
    }

    private void setUpPanel() {
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

        blueBorder.add(createHeaders(HEADER_ID));
        blueBorder.add(createListView(ITEM_LIST_ID));
    }

    private RepeatingView createHeaders(String id) {
        RepeatingView cells = new RepeatingView(id);

        for (EntrySelectorData.Header header : getPanelModelObject().getColumnHeaders()) {
            cells.add(new Label(cells.newChildId(), new ResourceModel(header.getResourceLabel())));
        }

        return cells;
    }

    private EntrySelectorListView createListView(String id) {
        return new EntrySelectorListView(id, getPanelModelObject().getRows(), clickHandler) {

            @Override
            protected void onPopulate(ListItem<EntrySelectorRow> item, IModel<EntrySelectorRow> itemModel) {
                EntrySelectorRow object = item.getModelObject();

                RepeatingView cells = new RepeatingView("child");

                int index = 0;

                for (Serializable serializable : object.getCells()) {
                    Label label = new Label(cells.newChildId(), serializable);
                    cells.add(label);

                    EntrySelectorData.Header header = getPanelModelObject().getColumnHeaders().get(index);

                    if (header.getColumnType() == EntrySelectorData.ColumnType.NUMERIC) {
                        label.add(AttributeModifier.replace("class", "numeric"));
                    }

                    if (!object.isActive()) {
                        label.add(AttributeModifier.append("class", "inactive"));
                    }

                    index++;
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

        addHideInactiveFilter(form);

        XlsxLink excelLink = createExcelLink();

        form.add(excelLink);

        return form;
    }

    private XlsxLink createExcelLink() {
        return new XlsxLink("toExcel", "out.xlsx", new IWriteBytes() {
                @Override
                public void write(OutputStream stream) throws IOException {
                    EntrySelectorExcelGenerator excelGenerator = new EntrySelectorExcelGenerator();
                    ExcelWorkbook workbook = excelGenerator.create(getPanelModelObject(), "Export");
                    workbook.write(stream);
                }
            });
    }

    private void addHideInactiveFilter(Form<Void> form) {
        final HideInactiveFilter hideInactiveFilter = new HideInactiveFilter();
        hideInactiveFilter.setHideInactive(getEhourWebSession().getHideInactiveSelections());

        final WebMarkupContainer filterIcon = new WebMarkupContainer("filterIcon");
        addFilterIconAttributes(filterIcon, getEhourWebSession().getHideInactiveSelections());
        filterIcon.setOutputMarkupId(true);

        final AjaxLink<Void> hideInactiveLink = new AjaxLink<Void>("filterToggle") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Boolean hideInactiveSelections = getEhourWebSession().toggleHideInactiveSelections();
                HideInactiveFilter inactiveFilter = new HideInactiveFilter(hideInactiveSelections);
                send(getPage(), Broadcast.DEPTH, new InactiveFilterChangedEvent(inactiveFilter, target));

                target.appendJavaScript(WINDOW_ENTRY_SELECTOR_REFRESH);

                filterIcon.removeAll();
                addFilterIconAttributes(filterIcon, getEhourWebSession().getHideInactiveSelections());
                target.add(filterIcon);
            }
        };

        hideInactiveLink.setVisible(showHideInactiveLink);
        hideInactiveLink.add(filterIcon);
        form.add(hideInactiveLink);
    }

    private void addFilterIconAttributes(WebMarkupContainer filterIcon, boolean active) {
        filterIcon.add(AttributeModifier.replace("title", hideInactiveCheckboxTooltip));
        filterIcon.add(AttributeModifier.replace("class", active ? "fa fa-toggle-on" : "fa fa-toggle-off"));
    }

    private static final long serialVersionUID = -7928428437664050056L;

    public interface ClickHandler extends Serializable {
        void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException;
    }
}
