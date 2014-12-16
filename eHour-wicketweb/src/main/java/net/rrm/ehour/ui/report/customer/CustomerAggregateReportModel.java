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

package net.rrm.ehour.ui.report.customer;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.common.report.AggregatedReportConfig;
import net.rrm.ehour.ui.report.aggregate.AbstractAggregateReportModel;
import net.rrm.ehour.ui.report.aggregate.node.CustomerNode;
import net.rrm.ehour.ui.report.aggregate.node.ProjectNode;
import net.rrm.ehour.ui.report.aggregate.node.UserEndNode;
import net.rrm.ehour.ui.report.model.ReportNode;
import net.rrm.ehour.ui.report.model.ReportNodeFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomerAggregateReportModel extends AbstractAggregateReportModel {
    private static final long serialVersionUID = -3221674649410450972L;

    public CustomerAggregateReportModel(ReportCriteria reportCriteria) {
        super(reportCriteria, AggregatedReportConfig.AGGREGATE_CUSTOMER);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ReportData preprocess(ReportData reportData, ReportCriteria reportCriteria) {
        List<AssignmentAggregateReportElement> reportElements = (List<AssignmentAggregateReportElement>) reportData.getReportElements();

        Collections.sort(reportElements, new Comparator<AssignmentAggregateReportElement>() {
            @Override
            public int compare(AssignmentAggregateReportElement o1, AssignmentAggregateReportElement o2) {
                return o1.getProjectAssignment().getProject().getCustomer().compareTo(o2.getProjectAssignment().getProject().getCustomer());
            }
        });

        return new ReportData(reportData.getLockedDays(), reportElements, reportData.getReportRange(), reportCriteria.getUserSelectedCriteria());
    }

    @Override
    public ReportNodeFactory<AssignmentAggregateReportElement> getReportNodeFactory() {
        return new ReportNodeFactory<AssignmentAggregateReportElement>() {
            @Override
            public ReportNode createReportNode(AssignmentAggregateReportElement aggregate, int hierarchyLevel) {
                switch (hierarchyLevel) {
                    case 0:
                        return (aggregate != null) ? new CustomerNode(aggregate) : null;
                    case 1:
                        return new ProjectNode(aggregate);
                    case 2:
                        return new UserEndNode(aggregate);
                }

                throw new RuntimeException("Hierarchy level too deep");
            }

            /**
             * Only needed for the root node, customer
             * @param aggregate
             * @return
             */
            public Serializable getElementId(AssignmentAggregateReportElement aggregate) {
                return aggregate.getProjectAssignment().getProject().getCustomer().getPK();
            }
        };
    }
}
