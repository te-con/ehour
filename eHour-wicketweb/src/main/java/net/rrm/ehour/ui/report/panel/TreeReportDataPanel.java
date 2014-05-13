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

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.border.BlueTabRoundedBorder;
import net.rrm.ehour.ui.common.component.CurrencyLabel;
import net.rrm.ehour.ui.common.component.HoverPagingNavigator;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.report.*;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.ui.common.wicket.Container;
import net.rrm.ehour.ui.report.TreeReportDataProvider;
import net.rrm.ehour.ui.report.TreeReportElement;
import net.rrm.ehour.ui.report.TreeReportModel;
import net.rrm.ehour.ui.report.panel.detail.AggregateByChangedEvent;
import net.rrm.ehour.ui.report.summary.ProjectSummaryPage;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.IConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate report data panel
 */

public class TreeReportDataPanel extends AbstractBasePanel<ReportData> {
    private static final long serialVersionUID = -6757047600645464803L;
    private static final AttributeModifier CSS_ALIGN_RIGHT = AttributeModifier.replace("style", "text-align: right;");
    private static final String REPORT_CONTENT_ID = "reportData";
    private static final String REPORT_DATA_ID = "reportData";
    private static final String NAVIGATOR_ID = "navigator";
    private final ExcelReport excelReport;

    private ReportConfig reportConfig;
    private Container reportTableContainer;
    private UserSelectedCriteria criteria;
    private final Border blueBorder;

    public TreeReportDataPanel(String id,
                               TreeReportModel reportModel,
                               ReportConfig reportConfig,
                               ExcelReport excelReport) {
        super(id, reportModel);
        this.excelReport = excelReport;

        criteria = reportModel.getReportCriteria().getUserSelectedCriteria();

        setOutputMarkupId(true);

        this.reportConfig = reportConfig;

        blueBorder = new BlueTabRoundedBorder("blueFrame");
        add(blueBorder);

        blueBorder.add(getReportHeaderLabel("reportHeader", reportModel.getReportRange(), EhourWebSession.getEhourConfig()));

        WebMarkupContainer reportContent = createReportContent(reportModel, excelReport, REPORT_CONTENT_ID);
        blueBorder.add(reportContent);
    }

    private WebMarkupContainer createReportContent(TreeReportModel reportModel, ExcelReport excelReport, String id) {
        boolean emptyReport = reportModel.getReportData().isEmpty();
        WebMarkupContainer reportContent = emptyReport ? new Fragment(id, "noData", this) : createReport(REPORT_CONTENT_ID, reportModel, excelReport);
        reportContent.setOutputMarkupId(true);
        return reportContent;
    }

    private Fragment createReport(String id, final TreeReportModel reportModel, final ExcelReport excelReport) {
        final Fragment reportTable = new Fragment(id, "reportTable", this);

        reportTable.add(createExcelLink(reportModel, excelReport));

        WebMarkupContainer optionsFilter = new WebMarkupContainer("optionsToggle");
        optionsFilter.setVisible(reportConfig.isShowZeroBookings());
        reportTable.add(optionsFilter);

        reportTable.add(createZeroBookingSelector("reportOptionsPlaceholder"));
        reportTable.add(createAdditionalOptions("additionalOptions"));

        reportTableContainer = createReportTableContainer(reportModel);
        reportTable.add(reportTableContainer);

        return reportTable;
    }

    private Container createReportTableContainer(TreeReportModel reportModel) {
        Container reportTableContainer = new Container("reportTableContainer");

        reportTableContainer.add(addHeaderColumns("columnHeaders"));
        addReportData(reportModel, reportTableContainer);
        reportTableContainer.add(addGrandTotal("cell", reportModel));

        return reportTableContainer;
    }

    private WebMarkupContainer createZeroBookingSelector(String id) {
        if (reportConfig.isShowZeroBookings()) {
            return new ZeroBookingSelector(id, reportConfig, criteria);
        } else {
            return new Container(id);
        }
    }

    protected WebMarkupContainer createAdditionalOptions(String id) {
        return new Container(id);
    }

    @Override
    public void onEvent(IEvent<?> event) {
        if (event.getPayload() instanceof ZeroBookingSelectionChangedEvent) {
            TreeReportModel model = (TreeReportModel) getPanelModel();
            model.detach();

            addReportData(model, reportTableContainer);

            ((ZeroBookingSelectionChangedEvent) event.getPayload()).target().add(reportTableContainer);
        } else if (event.getPayload() instanceof AggregateByChangedEvent) {
            AggregateByChangedEvent aggregateByChangedEvent = (AggregateByChangedEvent) event.getPayload();

            reportConfig = aggregateByChangedEvent.reportConfig();

            if (reportTableContainer != null) {
                Container container = createReportTableContainer((TreeReportModel) getPanelModel());

                reportTableContainer.replaceWith(container);
                reportTableContainer = container;
                aggregateByChangedEvent.target().add(reportTableContainer);
            }
        }
    }


    private Component createExcelLink(final TreeReportModel reportModel, final ExcelReport excelReport) {
        if (excelReport != null) {
            return new ExcelLink("excelLink", reportModel.getReportCriteria()) {
                @Override
                protected ExcelReport createReportBuilder() {
                    return excelReport;
                }
            };
        } else {
            return HtmlUtil.getInvisibleLink("excelLink");
        }
    }

    private RepeatingView addGrandTotal(String id, TreeReportModel reportModel) {
        RepeatingView totalView = new RepeatingView(id);

        // add cells
        for (ReportColumn column : reportConfig.getReportColumns()) {
            if (column.isVisible()) {
                Label label;

                String childId = totalView.newChildId();

                switch (column.getColumnType()) {
                    case HOUR:
                        label = new Label(childId, new Model<Float>(reportModel.getTotalHours()));
                        break;
                    case TURNOVER:
                        label = new CurrencyLabel(childId, reportModel.getTotalTurnover());
                        label.setEscapeModelStrings(false);
                        break;
                    default:
                        label = HtmlUtil.getNbspLabel(childId);
                        break;
                }

                Optional<String> optionalClass = addColumnTypeStyling(column.getColumnType());
                if (optionalClass.isPresent()) {
                    label.add(AttributeModifier.append("class", optionalClass.get()));
                }

                totalView.add(label);
            }
        }

        return totalView;
    }

    private Label getReportHeaderLabel(String id, DateRange reportRange, EhourConfig config) {
        Label reportHeaderLabel = new Label(id, new StringResourceModel("report.header",
                this, null,
                new Object[]{new DateModel(reportRange.getDateStart(), config),
                        new DateModel(reportRange.getDateEnd(), config)}
        ));
        reportHeaderLabel.setEscapeModelStrings(false);

        return reportHeaderLabel;
    }

    @SuppressWarnings("unchecked")
    private void addReportData(TreeReportModel reportModel, WebMarkupContainer parent) {
        List<TreeReportElement> elements = (List<TreeReportElement>) reportModel.getReportData().getReportElements();

        DataView<TreeReportElement> dataView = new TreeReportDataView(REPORT_DATA_ID, new TreeReportDataProvider(elements));
        dataView.setOutputMarkupId(true);
        dataView.setItemsPerPage(25);

        parent.addOrReplace(new HoverPagingNavigator(NAVIGATOR_ID, dataView));
        parent.addOrReplace(dataView);
    }


    private RepeatingView addHeaderColumns(String id) {
        RepeatingView columnHeaders = new RepeatingView(id);

        for (ReportColumn reportColumn : reportConfig.getReportColumns()) {
            Label columnHeader = new Label(columnHeaders.newChildId(), new ResourceModel(reportColumn.getColumnHeaderResourceKey()));
            columnHeader.setVisible(reportColumn.isVisible());
            columnHeaders.add(columnHeader);

            if (reportColumn.getColumnType().isNumeric()) {
                columnHeader.add(CSS_ALIGN_RIGHT);
            }
        }

        return columnHeaders;
    }

    private Optional<String> addColumnTypeStyling(ColumnType columnType) {
        if (columnType.isNumeric()) {
            return Optional.of("numeric");
        } else if (columnType == ColumnType.COMMENT) {
            return Optional.of("comment");
        } else {
            return Optional.absent();
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
            RepeatingView cells = new RepeatingView("cell");
            TreeReportElement row = item.getModelObject();

            if (row.isEmpty()) {
                item.add(AttributeModifier.append("class", "emptyRow"));
            }

            List<Serializable> thisCellValues = new ArrayList<Serializable>();

            boolean newValueInPreviousColumn = false;

            int column = 0;

            // add cells for a row
            for (final Serializable cellValue : row.getRow()) {
                thisCellValues.add(cellValue);

                ReportColumn reportColumn = reportConfig.getReportColumns()[column];

                if (reportColumn.isVisible()) {
                    List<String> cssClasses = new ArrayList<String>();

                    Label cellLabel;

                    final String id = Integer.toString(column);

                    if (isDuplicate(column, cellValue) && !newValueInPreviousColumn) {
                        cellLabel = new Label(id, new Model<String>(""));
                        newValueInPreviousColumn = false;

                        cells.add(cellLabel);
                    } else if (reportColumn.getColumnType() == ColumnType.LINK) {
                        Fragment linkFragment = new Fragment(id, "linkFragment", TreeReportDataPanel.this);
                        cells.add(linkFragment);

                        Link link = new Link("link") {

                            @Override
                            public void onClick() {
                                PageParameters pageParameters = new PageParameters();
                                pageParameters.add("id", cellValue);

                                setResponsePage(ProjectSummaryPage.class, pageParameters);
                            }
                        };

                        linkFragment.add(link);

                        cellLabel = new Label("linkLabel", new Model<Serializable>(cellValue));
                        link.add(cellLabel);

                    } else if (reportColumn.getConverter() != null) {
                        final IConverter converter = reportColumn.getConverter();

                        cellLabel = new Label(id, new Model<Serializable>(cellValue)) {
                            @SuppressWarnings("unchecked")
                            @Override
                            public <C> IConverter<C> getConverter(Class<C> type) {
                                return converter;
                            }
                        };

                        Optional<String> optionalClass = addColumnTypeStyling(reportColumn.getColumnType());
                        if (optionalClass.isPresent()) {
                            cssClasses.add(optionalClass.get());
                        }


                        newValueInPreviousColumn = true;

                        cells.add(cellLabel);
                    } else {
                        newValueInPreviousColumn = true;

                        cellLabel = new Label(id, new Model<Serializable>(cellValue));
                        Optional<String> optionalClass = addColumnTypeStyling(reportColumn.getColumnType());
                        if (optionalClass.isPresent()) {
                            cssClasses.add(optionalClass.get());
                        }

                        cells.add(cellLabel);
                    }

                    cssClasses.add(item.getIndex() == 0 ? "firstRow" : "");
                    cssClasses.add(column == 0 ? " firstColumn" : "");

                    String cssClass = Joiner.on(" ").join(cssClasses);

                    if (StringUtils.isNotEmpty(cssClass)) {
                        cellLabel.add(AttributeModifier.replace("class", cssClass));
                    }
                }

                column++;
            }

            item.add(cells);

            modifyClassForOddRows(item);
            previousForPage = getCurrentPage();
            previousCellValues = thisCellValues;
        }


        private void modifyClassForOddRows(Item<?> item) {
            if (item.getIndex() % 2 == 1) {
                item.add(AttributeModifier.append("class", "oddRow"));
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
