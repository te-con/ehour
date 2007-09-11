package net.rrm.ehour.ui.report.aggregate;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;
import net.rrm.ehour.ui.report.aggregate.value.ReportNodeFactory;

import java.io.Serializable;
import java.util.List;

/**
 * User: Thies
 * Date: Sep 11, 2007
 * Time: 9:26:29 PM
 * Copyright (C) 2005-2007 TE-CON, All Rights Reserved.
 * <p/>
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is
 * available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for
 * commercial use or open source, is subject to obtaining the prior express authorization of TE-CON.
 * <p/>
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 */
public class CustomerAggregateReport implements AggregateReport
{
    private List<ReportNode>    nodes;

    /**
     *
     * @param reportDataAggregate
     */
    public CustomerAggregateReport(ReportDataAggregate reportDataAggregate)
    {
        ReportBuilder    reportBuilder = new ReportBuilder();
        nodes = reportBuilder.createReport(reportDataAggregate, new CustomerReportNodeFactory());
    }

    /**
     *
     * @param reportDataAggregate
     */
    public CustomerAggregateReport(ReportDataAggregate reportDataAggregate, Integer forId)
    {
        ReportBuilder    reportBuilder = new ReportBuilder();
        nodes = reportBuilder.createReport(reportDataAggregate, forId, new CustomerReportNodeFactory());
    }

    /**
     * Get nodes
     * @return
     */
    public List<ReportNode> getNodes()
    {
        return nodes;
    }

    /**
     *
     */
    private class CustomerReportNodeFactory extends ReportNodeFactory
    {
        @Override
        public ReportNode createReportNode(ProjectAssignmentAggregate aggregate, int hierarchyLevel)
        {
            switch (hierarchyLevel)
            {
                case 0:
                    return new CustomerNode(aggregate);
                case 1:
                    return new ProjectNode(aggregate);
                case 2:
                    return new EndNode(aggregate);
            }

            throw new RuntimeException("Hierarchy level too deep");
        }

        /**
         * Only needed for the root node, customer
         * @param aggregate
         * @return
         */

        public Serializable getAssignmentId(ProjectAssignmentAggregate aggregate)
        {
            return aggregate.getProjectAssignment().getProject().getCustomer().getPK();
        }
    }

    /**
     * Customer node displaying customer full name
     */
    private class CustomerNode extends ReportNode
    {
        private CustomerNode(ProjectAssignmentAggregate aggregate)
        {
            this.id = aggregate.getProjectAssignment().getProject().getCustomer().getPK();
            this.columnValues = new String[]{aggregate.getProjectAssignment().getProject().getCustomer().getFullName()};
        }

        protected Serializable getAggregateId(ProjectAssignmentAggregate aggregate)
        {
            return aggregate.getProjectAssignment().getProject().getCustomer().getPK();
        }
    }

    /**
     * Project node displaying project full name 
     */
    private class ProjectNode extends ReportNode
    {
        private ProjectNode(ProjectAssignmentAggregate aggregate)
        {
            this.id = aggregate.getProjectAssignment().getProject().getPK();
            this.columnValues = new String[]{aggregate.getProjectAssignment().getProject().getName(),
                                             aggregate.getProjectAssignment().getProject().getProjectCode()};
        }

        @Override
        protected Serializable getAggregateId(ProjectAssignmentAggregate aggregate)
        {
            return aggregate.getProjectAssignment().getProject().getPK();
        }
    }

    /**
     * End node displaying user's full name, hours and turnover
     */
    private class EndNode extends ReportNode
    {
        private Number   hours;
        private Number   turnOver;

        private EndNode(ProjectAssignmentAggregate aggregate)
        {
            hours = aggregate.getHours();
            turnOver = aggregate.getTurnOver();

            this.id = aggregate.getProjectAssignment().getUser().getPK();
            this.columnValues = new Serializable[]{aggregate.getProjectAssignment().getUser().getFullName(),
                                                    aggregate.getProjectAssignment().getHourlyRate(),
                                                    aggregate.getHours(),
                                                    aggregate.getTurnOver()};

        }

        @Override
        protected Serializable getAggregateId(ProjectAssignmentAggregate aggregate)
        {
            return aggregate.getProjectAssignment().getUser().getPK();
        }


        @Override
        public Number getHours()
        {
            return hours; 
        }

        @Override
        public Number getTurnover()
        {
            return turnOver;
        }

        @Override
        protected boolean isLastNode()
        {
            return true;
        }
    }
}
