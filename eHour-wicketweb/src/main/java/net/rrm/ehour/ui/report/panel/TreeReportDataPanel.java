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
import net.rrm.ehour.ui.common.border.BlueTabRoundedBorder;
import net.rrm.ehour.ui.common.component.CurrencyLabel;
import net.rrm.ehour.ui.common.component.HoverPagingNavigator;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.report.*;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.ui.report.TreeReportDataProvider;
import net.rrm.ehour.ui.report.TreeReportElement;
import net.rrm.ehour.ui.report.TreeReportModel;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.util.convert.IConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate report data panel
 */

public class TreeReportDataPanel extends Panel {
    private static final long serialVersionUID = -6757047600645464803L;
    private static final AttributeModifier CSS_ALIGN_RIGHT = AttributeModifier.replace("style", "text-align: right;");

    private final ReportConfig reportConfig;

    public TreeReportDataPanel(String id,
                               TreeReportModel reportModel,
                               ReportConfig reportConfig,
                               final ExcelReport excelReport
    ) {
        super(id);

        this.reportConfig = reportConfig;

        Border blueBorder = new BlueTabRoundedBorder("blueFrame");
        add(blueBorder);

        WebMarkupContainer reportContent = new WebMarkupContainer("reportContent");
        reportContent.setOutputMarkupId(true);
        blueBorder.add(reportContent);

        if (excelReport != null) {
            reportContent.add(new ExcelLink("excelLink", reportModel.getReportCriteria()) {
                @Override
                protected ExcelReport createReportBuilder() {
                    return excelReport;
                }
            });
        } else {
            reportContent.add(HtmlUtil.getInvisibleLink("excelLink"));
        }

        reportContent.add(getReportHeaderLabel("reportHeader", reportModel.getReportRange(), EhourWebSession.getSession().getEhourConfig()));
        addHeaderColumns(reportContent);
        addReportData(reportModel, reportContent);
        addGrandTotal(reportModel, reportContent);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(TreeReportDataPanel.class, "style/reportStyle.css")));
    }

    private void addGrandTotal(TreeReportModel reportModel, WebMarkupContainer parent) {
        RepeatingView totalView = new RepeatingView("cell");

        // add cells
        for (ReportColumn column : reportConfig.getReportColumns()) {
            if (column.isVisible()) {
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
            columnHeader.setVisible(reportColumn.isVisible());
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
                label.add(AttributeModifier.replace("style", "width: 300px;"));
            }
        }
    }


    private class TreeReportDataView extends DataView<TreeReportElement> {
        private static final long serialVersionUID = 1L;

        private long previousForPage = -1;
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

                if (reportColumn.isVisible()) {
                    Label cellLabel;

                    if (isDuplicate(column, cellValue) && !newValueInPreviousColumn) {
                        cellLabel = new Label(Integer.toString(column), new Model<String>(""));
                        newValueInPreviousColumn = false;
                    } else if (reportColumn.getConverter() != null) {
                        final IConverter converter = reportColumn.getConverter();

                        cellLabel = new Label(Integer.toString(column), new Model<Serializable>(cellValue)) {
                            @Override
                            public <C> IConverter<C> getConverter(Class<C> type) {
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
                        cellLabel.add(AttributeModifier.replace("class", cssClass));
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
                item.add(AttributeModifier.replace("style", "background-color: #fefeff"));
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
