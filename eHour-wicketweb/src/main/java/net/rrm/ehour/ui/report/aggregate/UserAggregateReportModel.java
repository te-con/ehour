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

package net.rrm.ehour.ui.report.aggregate;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.report.AggregatedReportConfig;
import net.rrm.ehour.ui.report.AbstractAggregateReportModel;
import net.rrm.ehour.ui.report.aggregate.node.CustomerNode;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.report.node.ReportNodeFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserAggregateReportModel extends AbstractAggregateReportModel {
    private static final long serialVersionUID = 2883703894793044411L;

    public UserAggregateReportModel(ReportCriteria reportCriteria) {
        super(reportCriteria, AggregatedReportConfig.AGGREGATE_USER);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ReportData preprocess(ReportData reportData, ReportCriteria reportCriteria) {
        List<ActivityAggregateReportElement> reportElements = (List<ActivityAggregateReportElement>) reportData.getReportElements();

        Collections.sort(reportElements, new Comparator<ActivityAggregateReportElement>() {
            @Override
            public int compare(ActivityAggregateReportElement o1, ActivityAggregateReportElement o2) {
                return o1.getActivity().getAssignedUser().compareTo(o2.getActivity().getAssignedUser());
            }
        });

        return new ReportData(reportData.getLockedDays(), reportElements, reportData.getReportRange(), reportCriteria.getUserSelectedCriteria());
    }

    @Override
    public ReportNodeFactory<ActivityAggregateReportElement> getReportNodeFactory() {
        return new ReportNodeFactory<ActivityAggregateReportElement>() {
            @Override
            public ReportNode createReportNode(ActivityAggregateReportElement aggregate, int hierarchyLevel) {
                switch (hierarchyLevel) {
                    case 0:
                        return new UserNode(aggregate);
                    case 1:
                        return new CustomerNode(aggregate);
                    case 2:
                        return new ProjectEndNode(aggregate);
                }

                throw new RuntimeException("Hierarchy level too deep");
            }

            /**
             * Only needed for the root node, user
             */

            public Serializable getElementId(ActivityAggregateReportElement aggregate) {
                return aggregate.getActivity().getAssignedUser().getPK();
            }
        };
    }

    private static final class UserNode extends ReportNode {
        private static final long serialVersionUID = 8534482324216994500L;

        private UserNode(ActivityAggregateReportElement aggregate) {
            super(aggregate.getActivity().getPK());
            this.columnValues = new Serializable[]{aggregate.getActivity().getAssignedUser().getFullName()};
        }

        @Override
        protected Serializable getElementId(ReportElement element) {
            ActivityAggregateReportElement aggregate = (ActivityAggregateReportElement) element;

            return aggregate.getActivity().getPK();
        }
    }

    private static final class ProjectEndNode extends ReportNode {
        private static final long serialVersionUID = 1L;

        private Number hours;

        private ProjectEndNode(ActivityAggregateReportElement aggregate) {
            super(aggregate.getActivity().getProject().getPK());
            hours = aggregate.getHours();

            this.columnValues = new Serializable[]{aggregate.getActivity().getProject().getName(),
                    aggregate.getActivity().getProject().getProjectCode(),
                    aggregate.getHours()};
        }

        @Override
        protected Serializable getElementId(ReportElement element) {
            ActivityAggregateReportElement aggregate = (ActivityAggregateReportElement) element;

            return aggregate.getActivity().getProject().getPK();
        }

        @Override
        public Number getHours() {
            return hours;
        }

        @Override
        protected boolean isLeaf() {
            return true;
        }
    }
}
