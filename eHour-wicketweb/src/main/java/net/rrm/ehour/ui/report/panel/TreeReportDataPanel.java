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

package net.rrm.ehour.ui.report.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.border.BlueTabRoundedBorder;
import net.rrm.ehour.ui.common.component.CurrencyLabel;
import net.rrm.ehour.ui.common.component.HoverPagingNavigator;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.report.ColumnType;
import net.rrm.ehour.ui.common.report.ReportColumn;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.ui.report.TreeReportDataProvider;
import net.rrm.ehour.ui.report.TreeReportElement;
import net.rrm.ehour.ui.report.TreeReportModel;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.value.ValueMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate report data panel
 */

public class TreeReportDataPanel extends Panel {
    private static final long serialVersionUID = -6757047600645464803L;
    private static final SimpleAttributeModifier CSS_ALIGN_RIGHT = new SimpleAttributeModifier("style", "text-align: right;");

    private final ReportConfig reportConfig;

    public TreeReportDataPanel(String id,
                               TreeReportModel reportModel,
                               ReportConfig reportConfig,
                               String excelResourceName
    ) {
        super(id);

        this.reportConfig = reportConfig;

        Border blueBorder = new BlueTabRoundedBorder("blueFrame");
        add(blueBorder);
        blueBorder.setOutputMarkupId(true);

        if (excelResourceName != null) {
            final String reportId = reportModel.getCacheId();

            ResourceReference excelResource = new ResourceReference(excelResourceName);
            ValueMap params = new ValueMap();
            params.add("reportId", reportId);
            ResourceLink<Void> excelLink = new ResourceLink<Void>("excelLink", excelResource, params);
            blueBorder.add(excelLink);
        } else {
            blueBorder.add(HtmlUtil.getInvisibleLink("excelLink"));
        }

        blueBorder.add(getReportHeaderLabel("reportHeader", reportModel.getReportRange(), EhourWebSession.getSession().getEhourConfig()));
        addHeaderColumns(blueBorder);
        addReportData(reportModel, blueBorder);
        addGrandTotal(reportModel, blueBorder);

        add(new StyleSheetReference("reportStyle", new CompressedResourceReference(TreeReportDataPanel.class, "style/reportStyle.css")));
    }

    private void addGrandTotal(TreeReportModel reportModel, WebMarkupContainer parent) {
        RepeatingView totalView = new RepeatingView("cell");

        // add cells
        for (ReportColumn column : reportConfig.getReportColumns()) {
            if (isReportColumnVisible(column)) {
                Label label;

                String id = totalView.newChildId();

                switch (column.getColumnType()) {
                    case HOUR:
                        label = new Label(id, new Model<Float>(reportModel.getTotalHours()));
                        break;
                    case TURNOVER:
                        label = new CurrencyLabel(id, reportModel.getTotalTurnover());
                        label.setEscapeModelStrings(false);
                        break;
                    default:
                        label = HtmlUtil.getNbspLabel(id);
                        break;
                }

                addColumnTypeStyling(column.getColumnType(), label);
                totalView.add(label);
            }
        }

        parent.add(totalView);
    }

    /**
     * Get report header label
     *
     * @param reportRange
     * @param config
     * @return
     */
    private Label getReportHeaderLabel(String id, DateRange reportRange, EhourConfig config) {
        Label reportHeaderLabel = new Label(id, new StringResourceModel("report.header",
                this, null,
                new Object[]{new DateModel(reportRange.getDateStart(), config),
                        new DateModel(reportRange.getDateEnd(), config)}));
        reportHeaderLabel.setEscapeModelStrings(false);

        return reportHeaderLabel;
    }

    /**
     * Get root node rows & cells
     */
    @SuppressWarnings("unchecked")
    private void addReportData(TreeReportModel reportModel, WebMarkupContainer parent) {
        List<TreeReportElement> elements = (List<TreeReportElement>) reportModel.getReportData().getReportElements();

        DataView<TreeReportElement> dataView = new TreeReportDataView("reportData", new TreeReportDataProvider(elements));
        dataView.setOutputMarkupId(true);
        dataView.setItemsPerPage(20);

        parent.add(new HoverPagingNavigator("navigator", dataView));
        parent.add(dataView);
    }


    private void addHeaderColumns(WebMarkupContainer parent) {
        RepeatingView columnHeaders = new RepeatingView("columnHeaders");

        for (ReportColumn reportColumn : reportConfig.getReportColumns()) {
            Label columnHeader = new Label(columnHeaders.newChildId(), new ResourceModel(reportColumn.getColumnHeaderResourceKey()));
            columnHeader.setVisible(isReportColumnVisible(reportColumn));
            columnHeaders.add(columnHeader);

            if (reportColumn.getColumnType().isNumeric()) {
                columnHeader.add(CSS_ALIGN_RIGHT);
            }

        }

        parent.add(columnHeaders);
    }

    /**
     * Add column type specific styling
     *
     * @param columnType
     * @param label
     */
    private void addColumnTypeStyling(ColumnType columnType, Label label) {
        if (label != null) {
            if (columnType.isNumeric()) {
                label.add(CSS_ALIGN_RIGHT);
            }

            if (columnType == ColumnType.COMMENT) {
                label.add(new SimpleAttributeModifier("style", "width: 300px;"));
            }
        }
    }


    private boolean isReportColumnVisible(ReportColumn reportColumn) {
        if (!reportColumn.isVisible()) {
            return false;
        }

        boolean isAuthorized = true;

        if (reportColumn.isRateRelated()) {
            EhourWebSession session = EhourWebSession.getSession();

            boolean showTurnover = session.getEhourConfig().isShowTurnover();

            if (!showTurnover) {
                Roles roles = session.getRoles();
                isAuthorized = roles.hasRole(UserRole.ROLE_REPORT);
            }
        }

        return isAuthorized;
    }

    private class TreeReportDataView extends DataView<TreeReportElement> {
        private static final long serialVersionUID = 1L;

        private int previousForPage = -1;
        private List<Serializable> previousCellValues;

        public TreeReportDataView(String id, IDataProvider<TreeReportElement> dataProvider) {
            super(id, dataProvider);
        }

        @Override
        protected void populateItem(Item<TreeReportElement> item) {
            internalGetItemCount();
            RepeatingView cells = new RepeatingView("cell");
            TreeReportElement row = item.getModelObject();

            List<Serializable> thisCellValues = new ArrayList<Serializable>();

            boolean newValueInPreviousColumn = false;

            int column = 0;

            // add cells for a row
            for (Serializable cellValue : row.getRow()) {
                thisCellValues.add(cellValue);

                ReportColumn reportColumn = reportConfig.getReportColumns()[column];

                if (isReportColumnVisible(reportColumn)) {
                    Label cellLabel;

                    if (isDuplicate(column, cellValue) && !newValueInPreviousColumn) {
                        cellLabel = new Label(Integer.toString(column), new Model<String>(""));
                        newValueInPreviousColumn = false;
                    } else if (reportColumn.getConverter() != null) {
                        final IConverter converter = reportColumn.getConverter();

                        cellLabel = new Label(Integer.toString(column), new Model<Serializable>(cellValue)) {
                            @Override
                            public IConverter getConverter(Class<?> type) {
                                return converter;
                            }
                        };

                        addColumnTypeStyling(reportColumn.getColumnType(), cellLabel);

                        newValueInPreviousColumn = true;
                    } else {
                        newValueInPreviousColumn = true;

                        IModel<Serializable> model = new Model<Serializable>(cellValue);

                        cellLabel = new Label(Integer.toString(column), model);
                        addColumnTypeStyling(reportColumn.getColumnType(), cellLabel);
                    }

                    StringBuilder cssClassBuilder = new StringBuilder();
                    cssClassBuilder.append(item.getIndex() == 0 ? "firstRow" : "");
                    cssClassBuilder.append(column == 0 ? " firstColumn" : "");

                    String cssClass = cssClassBuilder.toString();

                    if (StringUtils.isNotEmpty(cssClass)) {
                        cellLabel.add(new SimpleAttributeModifier("class", cssClass));
                    }

                    cells.add(cellLabel);
                }

                column++;
            }

            item.add(cells);

            setCssStyle(item);
            previousForPage = getCurrentPage();
            previousCellValues = thisCellValues;
        }


        private void setCssStyle(Item<?> item) {
            if (item.getIndex() % 2 == 1) {
                item.add(new SimpleAttributeModifier("style", "background-color: #fefeff"));
            }
        }

        private boolean isDuplicate(int i, Serializable cellValue) {
            return (!reportConfig.getReportColumns()[i].isAllowDuplicates()
                    && previousCellValues != null
                    && previousForPage == getCurrentPage()
                    && previousCellValues.get(i) != null
                    && previousCellValues.get(i).equals(cellValue));
        }
    }
}
