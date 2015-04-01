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
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.component.CurrencyLabel;
import net.rrm.ehour.ui.common.component.HoverPagingNavigator;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.report.*;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.ui.common.wicket.Container;
import net.rrm.ehour.ui.report.GreyReportBorder;
import net.rrm.ehour.ui.report.model.TreeReportDataProvider;
import net.rrm.ehour.ui.report.model.TreeReportElement;
import net.rrm.ehour.ui.report.model.TreeReportModel;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
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
    private static final String REPORT_CONTENT_ID = "reportContent";
    private static final String REPORT_TABLE_ID = "reportTable";
    private static final String NAVIGATOR_ID = "navigator";

    private ReportConfig reportConfig;
    private UserSelectedCriteria criteria;
    private HoverPagingNavigator pagingNavigator;
    private Container reportFrameContainer;
    private Fragment reportFragment;

    public TreeReportDataPanel(String id,
                               TreeReportModel reportModel,
                               ReportConfig reportConfig,
                               ExcelReport excelReport) {
        super(id, reportModel);
        setOutputMarkupId(true);

        this.reportConfig = reportConfig;
        criteria = reportModel.getReportCriteria().getUserSelectedCriteria();

        Border outerFrame = new GreyReportBorder("greyFrame");
        add(outerFrame);

        outerFrame.add(createReportHeaderLabel("reportHeader", reportModel.getReportRange(), EhourWebSession.getEhourConfig()));
        outerFrame.add(createReportContent(REPORT_CONTENT_ID, reportModel, excelReport));
    }

    // Report period: 10/1/13 - 12/31/13  label
    private Label createReportHeaderLabel(String id, DateRange reportRange, EhourConfig config) {
        Label reportHeaderLabel = new Label(id, new StringResourceModel("report.header",
                this, null,
                new Object[]{new DateModel(reportRange.getDateStart(), config),
                        new DateModel(reportRange.getDateEnd(), config)}
        ));
        reportHeaderLabel.setEscapeModelStrings(false);

        return reportHeaderLabel;
    }

    private WebMarkupContainer createReportContent(String id, TreeReportModel reportModel, ExcelReport excelReport) {
        boolean emptyReport = reportModel.getReportData().isEmpty();

        WebMarkupContainer reportContent = emptyReport ? noDataReport(id) : withDataReport(id, reportModel, excelReport);
        reportContent.setOutputMarkupId(true);

        return reportContent;
    }

    private Fragment noDataReport(String id) {
        return new Fragment(id, "noData", this);
    }

    private Fragment withDataReport(String id, final TreeReportModel reportModel, final ExcelReport excelReport) {
        reportFragment = new Fragment(id, "withDataReport", this);

        reportFragment.add(createExcelLink(excelReport));

        reportFragment.add(createZeroBookingSelector("reportOptionsPlaceholder"));
        reportFragment.add(createAdditionalOptions("additionalOptions"));

        createReportTableContainer(reportModel, reportFragment);

        return reportFragment;
    }

    private void createReportTableContainer(TreeReportModel reportModel, MarkupContainer parent) {
        Border reportFrame = new GreyBlueRoundedBorder("reportFrame") {
            @Override
            protected WebMarkupContainer createComponent() {
                WebMarkupContainer frame = super.createComponent();
                frame.add(AttributeModifier.append("style", "margin: 0 5px 0 5px;"));

                return frame;
            }
        };

        reportFrameContainer = new Container("reportFrameContainer");
        reportFrame.add(reportFrameContainer);

        reportFrameContainer.add(addHeaderColumns("columnHeaders"));

        DataView<TreeReportElement> dataView = createDataView(reportModel);
        reportFrameContainer.add(dataView);
        reportFrameContainer.add(addGrandTotal("cell", reportModel));

        pagingNavigator = createNav(dataView);
        parent.addOrReplace(pagingNavigator);
        parent.addOrReplace(reportFrame);
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
            updateDataTableForZeroBookingSelectionChange(event);
        } else if (event.getPayload() instanceof UpdateReportDataEvent) {
            updateReport(event);
        }
    }

    private void updateReport(IEvent<?> event) {
        UpdateReportDataEvent aggregateByChangedEvent = (UpdateReportDataEvent) event.getPayload();

        reportConfig = aggregateByChangedEvent.getReportConfig();

        createReportTableContainer((TreeReportModel) getPanelModel(), reportFragment);
        aggregateByChangedEvent.target().add(reportFragment);
    }

    private void updateDataTableForZeroBookingSelectionChange(IEvent<?> event) {
        TreeReportModel model = (TreeReportModel) getPanelModel();
        model.detach();

        DataView<TreeReportElement> dataView = createDataView(model);
        reportFrameContainer.addOrReplace(dataView);

        HoverPagingNavigator nav = createNav(dataView);
        pagingNavigator.replaceWith(nav);
        pagingNavigator = nav;

        ZeroBookingSelectionChangedEvent payload = (ZeroBookingSelectionChangedEvent) event.getPayload();
        payload.target().add(reportFrameContainer, nav);
    }

    private HoverPagingNavigator createNav(DataView<TreeReportElement> dataView) {
        HoverPagingNavigator nav = new HoverPagingNavigator(NAVIGATOR_ID, dataView);
        nav.setOutputMarkupPlaceholderTag(true);
        return nav;
    }

    private Component createExcelLink(final ExcelReport excelReport) {
        if (excelReport != null) {
            return new ExcelReportLink("excelLink") {
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
                        label = new Label(childId, new Model<>(reportModel.getTotalHours()));
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

    @SuppressWarnings("unchecked")
    private DataView<TreeReportElement> createDataView(TreeReportModel reportModel) {
        List<TreeReportElement> elements = (List<TreeReportElement>) reportModel.getReportData().getReportElements();

        DataView<TreeReportElement> dataView = new TreeReportDataView(REPORT_TABLE_ID, new TreeReportDataProvider(elements));
        dataView.setOutputMarkupId(true);
        dataView.setItemsPerPage(25);

        return dataView;
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

            List<Serializable> thisCellValues = new ArrayList<>();

            boolean newValueInPreviousColumn = false;

            int column = 0;

            // add cells for a row
            for (final Serializable cellValue : row.getRow()) {
                thisCellValues.add(cellValue);

                ReportColumn reportColumn = reportConfig.getReportColumns()[column];

                if (reportColumn.isVisible()) {
                    List<String> cssClasses = new ArrayList<>();

                    Label cellLabel;

                    final String id = Integer.toString(column);

                    if (isDuplicate(column, cellValue) && !newValueInPreviousColumn) {
                        cellLabel = new Label(id, new Model<>(""));
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
// unused for now
                            }
                        };

                        linkFragment.add(link);

                        cellLabel = new Label("linkLabel", new Model<>(cellValue));
                        link.add(cellLabel);

                    } else if (reportColumn.getConverter() != null) {
                        final IConverter converter = reportColumn.getConverter();

                        cellLabel = new Label(id, new Model<>(cellValue)) {
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

                        cellLabel = new Label(id, new Model<>(cellValue));
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
                        cellLabel.add(AttributeModifier.replace("class", cssClass.trim()));
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
