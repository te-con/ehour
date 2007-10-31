package net.rrm.ehour.ui.report.aggregate;

import java.io.Serializable;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.report.aggregate.node.CustomerNode;
import net.rrm.ehour.ui.report.aggregate.node.ProjectNode;
import net.rrm.ehour.ui.report.aggregate.node.UserEndNode;
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
    	super(reportDataAggregate);
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
	                	if (aggregate != null)
	                	{
	                		return new CustomerNode(aggregate, 0);
	                	}
	                	else
	                	{
	                		return null;
	                	}
	                case 1:
	                    return new ProjectNode(aggregate, 1);
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
	
	        public Serializable getAssignmentId(ProjectAssignmentAggregate aggregate)
	        {
	            return aggregate.getProjectAssignment().getProject().getCustomer().getPK();
	        }
	    };
    }
}
