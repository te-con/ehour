package net.rrm.ehour.ui.report.aggregate;

import java.io.Serializable;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;
import net.rrm.ehour.ui.report.aggregate.value.ReportNodeFactory;

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
public class CustomerAggregateReport extends AggregateReport
{
	private static final long serialVersionUID = -3221674649410450972L;

    /**
     *
     * @param reportDataAggregate
     */
    public CustomerAggregateReport(ReportDataAggregate reportDataAggregate)
    {
    	this(reportDataAggregate, null);
    }

    /**
     *
     * @param reportDataAggregate
     */
    public CustomerAggregateReport(ReportDataAggregate reportDataAggregate, Integer forId)
    {
    	super(reportDataAggregate, forId);
    }

    /**
     *
     */
    public ReportNodeFactory getReportNodeFactory()
    {
    	return new ReportNodeFactory()
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
	    };
    }
    
    /**
     * Customer node displaying customer full name
     */
    private class CustomerNode extends ReportNode
    {
		private static final long serialVersionUID = -356525734449023397L;

		private CustomerNode(ProjectAssignmentAggregate aggregate)
        {
            this.id = aggregate.getProjectAssignment().getProject().getCustomer().getPK();
            this.columnValues = new String[]{aggregate.getProjectAssignment().getProject().getCustomer().getFullName()};
            this.hierarchyLevel = 0;
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
		private static final long serialVersionUID = -8068372785700592324L;

		private ProjectNode(ProjectAssignmentAggregate aggregate)
        {
            this.id = aggregate.getProjectAssignment().getProject().getPK();
            this.columnValues = new String[]{aggregate.getProjectAssignment().getProject().getName(),
                                             aggregate.getProjectAssignment().getProject().getProjectCode()};
            this.hierarchyLevel = 1;
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
		private static final long serialVersionUID = 3861923371702158088L;
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
            
            this.hierarchyLevel = 2;

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
