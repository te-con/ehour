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

package net.rrm.ehour.ui.admin.audit;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.component.HoverPagingNavigator;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.report.ExcelReport;
import net.rrm.ehour.ui.common.report.ExcelReportLink;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditReportDataPanel extends AbstractAjaxPanel<ReportCriteria> implements IHeaderContributor {
    private static final long serialVersionUID = -2380789244030608920L;

    public AuditReportDataPanel(String id, IModel<ReportCriteria> model) {
        super(id, model);

        setOutputMarkupId(true);

        addComponents(model);
    }

    private void addComponents(IModel<ReportCriteria> model) {
        Border greyBorder = new GreyBlueRoundedBorder("border");
        add(greyBorder);

        greyBorder.add(getPagingDataView(model));

        addExcelLink();
    }

    private void addExcelLink() {
        Link<?> excelLink = new ExcelReportLink("excelLink") {
            @Override
            protected ExcelReport createReportBuilder() {
                return new AuditReportExcel(getPanelModel());
            }
        };

        add(excelLink);
    }

    /**
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    private WebMarkupContainer getPagingDataView(IModel<ReportCriteria> model) {
        final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
        dataContainer.setOutputMarkupId(true);
        final EhourConfig config = EhourWebSession.getEhourConfig();


        List<IColumn<Audit, Date>> columns = new ArrayList<>();
        columns.add(new DateColumn(new ResourceModel("audit.report.column.date"), config));
        columns.add(new PropertyColumn<Audit, Date>(new ResourceModel("audit.report.column.lastName"), "userFullName"));
        columns.add(new PropertyColumn<Audit, Date>(new ResourceModel("audit.report.column.action"), "action"));
        columns.add(new PropertyColumn<Audit, Date>(new ResourceModel("audit.report.column.type"), "auditActionType.value"));


        AuditReportDataProvider dataProvider = new AuditReportDataProvider(getReportRequest(model));
        DataTable<Audit, Date> table = new DataTable<Audit, Date>("data", columns, dataProvider, 20) {
            @Override
            protected Item<Audit> newRowItem(String id, int index, IModel<Audit> model) {
                return new OddEvenItem<>(id, index, model);
            }
        };

        table.setOutputMarkupId(true);

        dataContainer.add(table);
        table.addTopToolbar(new AjaxFallbackHeadersToolbar(table, dataProvider));

        dataContainer.add(new HoverPagingNavigator("navigator", table));

        return dataContainer;
    }

    private AuditReportRequest getReportRequest(IModel<ReportCriteria> model) {
        ReportCriteria criteria = model.getObject();

        return (AuditReportRequest) criteria.getUserSelectedCriteria();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new CssResourceReference(AuditReportDataPanel.class, "auditStyle.css")));
    }

    private static class DateColumn extends AbstractColumn<Audit, Date> {
        private static final long serialVersionUID = -5517077439980001335L;
        private EhourConfig config;

        public DateColumn(IModel<String> displayModel, EhourConfig config) {
            super(displayModel);

            this.config = config;
        }

        public void populateItem(Item<ICellPopulator<Audit>> item, String componentId, IModel<Audit> model) {
            Date date = model.getObject().getDate();
            item.add(new Label(componentId, new DateModel(date, config, DateModel.DATESTYLE_DATE_TIME)));
        }
    }
}
