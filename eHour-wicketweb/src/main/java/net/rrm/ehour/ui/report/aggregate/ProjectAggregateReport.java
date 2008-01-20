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

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.panel.report.ReportConfig;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.aggregate.node.CustomerNode;
import net.rrm.ehour.ui.report.aggregate.node.ProjectNode;
import net.rrm.ehour.ui.report.aggregate.node.UserEndNode;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.report.node.ReportNodeFactory;

/**
 * TODO 
 **/

public class ProjectAggregateReport extends TreeReport<AssignmentAggregateReportElement>
{
	private static final long serialVersionUID = 6073113076906501807L;

	/**
	 * 
	 * @param reportData
	 */
	public ProjectAggregateReport(ReportData<AssignmentAggregateReportElement> reportData)
	{
		super(reportData, ReportConfig.AGGREGATE_PROJECT);
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
	        public ReportNode createReportNode(ReportElement element, int hierarchyLevel)
	        {
	        	AssignmentAggregateReportElement aggregate = (AssignmentAggregateReportElement)element;
	        	
	            switch (hierarchyLevel)
	            {
	                case 0:
	                    return new ProjectNode(aggregate, hierarchyLevel);
	                case 1:
	                    return new CustomerNode(aggregate, hierarchyLevel);
	                case 2:
	                    return new UserEndNode(aggregate, hierarchyLevel);
	            }
	
	            throw new RuntimeException("Hierarchy level too deep");
	        }
	
	        /**
	         * Only needed for the root node, customer
	         * @param aggregate
	         * @return
	         */
	
	        public Serializable getElementId(ReportElement element)
	        {
	        	AssignmentAggregateReportElement aggregate = (AssignmentAggregateReportElement)element;
	        	
	            return aggregate.getProjectAssignment().getProject().getPK();
	        }
	    };
	}
}
