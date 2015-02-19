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
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.report.excel.ExcelWorkbook;
import net.rrm.ehour.ui.common.report.excel.IWriteBytes;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.wicket.Container;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
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
import java.util.Date;
import java.util.UUID;

import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData.EntrySelectorRow;

/**
 * Selector with autocompletion filter
 */

public class EntrySelectorPanel extends AbstractBasePanel<EntrySelectorData> {
    private static final JavaScriptResourceReference JS = new JavaScriptResourceReference(EntrySelectorPanel.class, "entrySelector.js");
    private static final String ITEM_LIST_ID = "itemList";
    private static final String HEADER_ID = "headers";
    private final ClickHandler clickHandler;
    private final boolean wide;

    private IModel<String> hideInactiveLinkTooltip;
    private boolean showHideInactiveLink = false;
    private WebMarkupContainer listContainer;

    private final String frameDomId;
    private final String jsObjectId;

    public EntrySelectorPanel(String id, EntrySelectorData entrySelectorData, ClickHandler clickHandler) {
        this(id, entrySelectorData, clickHandler, null);
    }

    public EntrySelectorPanel(String id, EntrySelectorData entrySelectorData, ClickHandler clickHandler, IModel<String> hideInactiveLinkTooltip) {
        this(id, entrySelectorData, clickHandler, hideInactiveLinkTooltip, false);
    }

    public EntrySelectorPanel(String id, EntrySelectorData entrySelectorData, ClickHandler clickHandler, IModel<String> hideInactiveLinkTooltip, boolean wide) {
        super(id, new Model<>(entrySelectorData));

        frameDomId = "entrySelectorFrame_" + randomId();
        jsObjectId = "entrySelector_" + randomId();

        this.clickHandler = clickHandler;
        this.wide = wide;

        if (hideInactiveLinkTooltip != null) {
            this.hideInactiveLinkTooltip = hideInactiveLinkTooltip;
            showHideInactiveLink = true;
        }
    }

    private static String randomId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
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

        listContainer.addOrReplace(createListView(ITEM_LIST_ID));
    }

    public void reRender(AjaxRequestTarget target) {
        target.add(listContainer);
        target.appendJavaScript(jsRefresh());
    }

    private String jsRefresh() {
        return "window." + jsObjectId + ".refresh();";
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(JS));
        response.render(OnDomReadyHeaderItem.forScript("window." + jsObjectId + " = new EntrySelector('#" + frameDomId + "', '#listFilter', '.entrySelectorTable');"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        WebMarkupContainer selectorFrame = new WebMarkupContainer("entrySelectorFrame");
        selectorFrame.setMarkupId(frameDomId);
        selectorFrame.setOutputMarkupId(true);

        if (wide) {
            selectorFrame.add(AttributeModifier.append("style", "width: 97%;"));
        }

        addOrReplace(selectorFrame);

        selectorFrame.add(createForm());

        GreyBlueRoundedBorder border = new GreyBlueRoundedBorder("blueBorder") {
            @Override
            protected WebMarkupContainer createComponent() {
                WebMarkupContainer component = super.createComponent();

                component.add(AttributeModifier.append("class", "inline"));

                if (wide) {
                    component.add(AttributeModifier.append("style", "width: 100%;"));
                }

                return component;
            }
        };

        if (wide) {
            border.add(AttributeModifier.append("style", "width: 100%;"));
        }

        border.setOutputMarkupId(true);
        selectorFrame.add(border);

        listContainer = new Container("listScroll");
        listContainer.setMarkupId("listContents");

        if (!wide) {
            listContainer.add(AttributeModifier.append("class", "limitWidth"));
        } else {
            listContainer.add(AttributeModifier.append("style", "width: 99%"));
        }

        border.add(listContainer);

        listContainer.add(createHeaders(HEADER_ID));
        listContainer.add(createListView(ITEM_LIST_ID));
    }

    private RepeatingView createHeaders(String id) {
        RepeatingView cells = new RepeatingView(id);

        for (EntrySelectorData.Header header : getPanelModelObject().getColumnHeaders()) {
            Label label = new Label(cells.newChildId(), new ResourceModel(header.getResourceLabel()));
            if (header.getColumnType() == EntrySelectorData.ColumnType.NUMERIC ||
                    header.getColumnType() == EntrySelectorData.ColumnType.DATE) {
                label.add(AttributeModifier.replace("class", "numeric"));
            }
            cells.add(label);
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
                    EntrySelectorData.Header header = getPanelModelObject().getColumnHeaders().get(index);
                    EntrySelectorData.ColumnType columnType = header.getColumnType();

                    Label label;
                    if (columnType == EntrySelectorData.ColumnType.DATE) {
                        Date date = (Date) serializable;

                        DateModel dateModel;
                        dateModel = new DateModel(date, EhourWebSession.getEhourConfig(), DateModel.DATESTYLE_FULL_SHORT);
                        label = new Label(cells.newChildId(), dateModel);
                        label.setEscapeModelStrings(false);
                    } else {
                        label = new Label(cells.newChildId(), serializable);
                    }

                    if (columnType == EntrySelectorData.ColumnType.HTML) {
                        label.setEscapeModelStrings(false);
                    }

                    cells.add(label);


                    if (columnType == EntrySelectorData.ColumnType.NUMERIC) {
                        label.add(AttributeModifier.replace("class", "numeric"));
                    } else if (columnType == EntrySelectorData.ColumnType.DATE) {
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
        Form<Void> form = new Form<>("filterForm");

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

        onFiltersCreated(filterInputContainer);

        return form;
    }

    protected void onFiltersCreated(WebMarkupContainer filterInputContainer) {

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

                target.appendJavaScript(jsRefresh());

                filterIcon.removeAll();
                addFilterIconAttributes(filterIcon, getEhourWebSession().getHideInactiveSelections());
                target.add(filterIcon);
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
            }
        };

        hideInactiveLink.setVisible(showHideInactiveLink);
        hideInactiveLink.add(filterIcon);
        form.add(hideInactiveLink);
    }

    private void addFilterIconAttributes(WebMarkupContainer filterIcon, boolean active) {
        filterIcon.add(AttributeModifier.replace("title", hideInactiveLinkTooltip));
        filterIcon.add(AttributeModifier.replace("class", active ? "fa fa-toggle-on" : "fa fa-toggle-off"));
    }

    private static final long serialVersionUID = -7928428437664050056L;

    public interface ClickHandler extends Serializable {
        void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException;
    }

    public static class EntrySelectorBuilder {
        private EntrySelectorData entrySelectorData;
        private final String id;
        private ClickHandler clickHandler;
        private IModel<String> hideInactiveLinkTooltip;
        private boolean wide = false;

        public EntrySelectorBuilder(String id) {
            this.id = id;
        }

        public static EntrySelectorBuilder startAs(String id) {
            return new EntrySelectorBuilder(id);
        }

        public EntrySelectorBuilder withData(EntrySelectorData entrySelectorData) {
            this.entrySelectorData = entrySelectorData;
            return this;
        }

        public EntrySelectorBuilder onClick(ClickHandler clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        public EntrySelectorBuilder withInactiveTooltip(IModel<String> hideInactiveLinkTooltip) {
            this.hideInactiveLinkTooltip = hideInactiveLinkTooltip;
            return this;
        }

        public EntrySelectorBuilder isWide() {
            this.wide = true;
            return this;
        }

        public EntrySelectorPanel build() {
            return new EntrySelectorPanel(id, entrySelectorData, clickHandler, hideInactiveLinkTooltip, wide);
        }
    }
}
