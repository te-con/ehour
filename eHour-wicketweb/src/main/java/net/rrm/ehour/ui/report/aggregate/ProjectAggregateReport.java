/**
 * Created on Sep 27, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.report.aggregate;

import java.io.Serializable;

import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.report.reports.dto.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.report.aggregate.node.CustomerNode;
import net.rrm.ehour.ui.report.aggregate.node.ProjectNode;
import net.rrm.ehour.ui.report.aggregate.node.UserEndNode;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;
import net.rrm.ehour.ui.report.aggregate.value.ReportNodeFactory;

/**
 * TODO 
 **/

public class ProjectAggregateReport extends AggregateReport
{
	private static final long serialVersionUID = 6073113076906501807L;

	/**
	 * 
	 * @param reportDataAggregate
	 */
	public ProjectAggregateReport(ReportDataAggregate reportDataAggregate)
	{
		super(reportDataAggregate);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.aggregate.AggregateReport#getReportNodeFactory()
	 */
	@Override
	public ReportNodeFactory getReportNodeFactory()
	{
    	return new ReportNodeFactory()
	    {
	        @Override
	        public ReportNode createReportNode(AssignmentAggregateReportElement aggregate, int hierarchyLevel)
	        {
	            switch (hierarchyLevel)
	            {
	                case 0:
	                    return new ProjectNode(aggregate, 0);
	                case 1:
	                    return new CustomerNode(aggregate, 1);
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
	
	        public Serializable getAssignmentId(AssignmentAggregateReportElement aggregate)
	        {
	            return aggregate.getProjectAssignment().getProject().getPK();
	        }
	    };
	}
}
