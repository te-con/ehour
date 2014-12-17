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

package net.rrm.ehour.ui.report.detailed;

import com.google.common.collect.Maps;
import net.rrm.ehour.report.criteria.AggregateBy;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.report.DetailedReportConfig;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.ui.report.detailed.node.*;
import net.rrm.ehour.ui.report.model.ReportNode;
import net.rrm.ehour.ui.report.model.ReportNodeFactory;
import net.rrm.ehour.ui.report.model.TreeReportModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Detailed report
 */

public class DetailedReportModel extends TreeReportModel {
    private static final long serialVersionUID = -21703820501429504L;

    private static final Map<AggregateBy, AggregateConverter> AGGREGATE_MAP = Maps.newHashMap();

    static {
        AGGREGATE_MAP.put(AggregateBy.WEEK, new ByWeek());
        AGGREGATE_MAP.put(AggregateBy.MONTH, new ByMonth());
        AGGREGATE_MAP.put(AggregateBy.QUARTER, new ByQuarter());
        AGGREGATE_MAP.put(AggregateBy.YEAR, new ByYear());
    }

    @SpringBean(name = "detailedReportService")
    private DetailedReportService detailedReportService;

    public DetailedReportModel(ReportCriteria reportCriteria) {
        super(reportCriteria, DetailedReportConfig.DETAILED_REPORT_BY_DAY);
    }

    @Override
    protected ReportData fetchReportData(ReportCriteria reportCriteria) {
        return getDetailedReportService().getDetailedReportData(reportCriteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public  ReportData preprocess(ReportData reportData, ReportCriteria reportCriteria) {
        AggregateBy aggregateBy = reportCriteria.getUserSelectedCriteria().getAggregateBy();

        List<FlatReportElement> elements;
        List<FlatReportElement> originalElements = (List<FlatReportElement>) reportData.getReportElements();

        if (AGGREGATE_MAP.containsKey(aggregateBy)) {
            AggregateConverter aggregateFunction = AGGREGATE_MAP.get(aggregateBy);
            elements = DetailedReportAggregator.aggregate(originalElements, aggregateFunction);
        } else {
            elements = originalElements;
        }

        sortOnDate(elements);

        return new ReportData(reportData.getLockedDays(), elements, reportData.getReportRange(), reportCriteria.getUserSelectedCriteria());
    }

    private void sortOnDate(List<FlatReportElement> reportElements) {
        Collections.sort(reportElements, new Comparator<FlatReportElement>() {
            @Override
            public int compare(FlatReportElement o1, FlatReportElement o2) {

                if (o1.getDayDate() == null) {
                    return -1;
                } else if (o2.getDayDate() == null) {
                    return 1;
                } else {
                    return o1.getDayDate().compareTo(o2.getDayDate());
                }
            }
        });
    }

    private DetailedReportService getDetailedReportService() {
        if (detailedReportService == null) {
            WebUtils.springInjection(this);
        }

        return detailedReportService;
    }

    @Override
    public ReportNodeFactory<FlatReportElement> getReportNodeFactory() {
        return new ReportNodeFactory<FlatReportElement>() {
            @Override
            public ReportNode createReportNode(FlatReportElement flatElement, int hierarchyLevel) {
                switch (hierarchyLevel) {
                    case 0:
                        return new FlatLockableDateNode(flatElement);
                    case 1:
                        return new FlatCustomerNode(flatElement);
                    case 2:
                        return new FlatProjectNode(flatElement);
                    case 3:
                        return new FlatProjectCodeNode(flatElement);
                    case 4:
                        return new FlatUserNode(flatElement);
                    case 5:
                        return new FlatRoleNode(flatElement);
                    case 6:
                        return new FlatEntryEndNode(flatElement);
                }

                throw new RuntimeException("Hierarchy level too deep");
            }

            /**
             * Only needed for the root node, customer
             */
            public Serializable getElementId(FlatReportElement flatElement) {
                return flatElement.getCustomerId();
            }
        };
    }
}
