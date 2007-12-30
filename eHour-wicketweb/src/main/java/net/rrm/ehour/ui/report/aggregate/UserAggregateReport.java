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
import net.rrm.ehour.ui.report.aggregate.node.CustomerNode;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;
import net.rrm.ehour.ui.report.aggregate.value.ReportNodeFactory;

/**
 * TODO 
 **/

public class UserAggregateReport extends AggregateReport
{
	private static final long serialVersionUID = 2883703894793044411L;

	/**
	 * 
	 * @param reportData
	 */
	public UserAggregateReport(ReportData reportData)
	{
		super(reportData);
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
	                    return new UserNode(aggregate);
	                case 1:
	                    return new CustomerNode(aggregate, 1);
	                case 2:
	                    return new ProjectEndNode(aggregate);
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
	            return aggregate.getProjectAssignment().getUser().getPK();
	        }
	    };
	}

	/**
	 * 
	 * @author Thies
	 *
	 */
	private class UserNode extends ReportNode
	{
		private static final long serialVersionUID = 8534482324216994500L;

		private UserNode(AssignmentAggregateReportElement aggregate)
		{
	        this.id = aggregate.getProjectAssignment().getUser().getPK();
	        this.columnValues = new Serializable[]{aggregate.getProjectAssignment().getUser().getFullName()};
			
			this.hierarchyLevel = 0;
		}

		@Override
		protected Serializable getElementId(ReportElement element)
		{
			AssignmentAggregateReportElement aggregate = (AssignmentAggregateReportElement)element;
			
			return aggregate.getProjectAssignment().getUser().getPK();		
		}
	}
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private class ProjectEndNode extends ReportNode
	{
		private static final long serialVersionUID = 1L;
		
		private Number   hours;
	    private Number   turnOver;		
		
	    private ProjectEndNode(AssignmentAggregateReportElement aggregate)
	    {
	        hours = aggregate.getHours();
	        turnOver = aggregate.getTurnOver();

	        this.id = aggregate.getProjectAssignment().getUser().getPK();
	        this.id = aggregate.getProjectAssignment().getProject().getPK();
	        
	        this.columnValues = new Serializable[]{aggregate.getProjectAssignment().getProject().getName(),
	                                         		aggregate.getProjectAssignment().getProject().getProjectCode(),
	                                                aggregate.getProjectAssignment().getHourlyRate(),
	                                         		aggregate.getHours(),
	                                         		aggregate.getTurnOver()};
	        
	        this.hierarchyLevel = 2;

	    }
		
		@Override
		protected Serializable getElementId(ReportElement element)
		{
			AssignmentAggregateReportElement aggregate = (AssignmentAggregateReportElement)element;
			
			return aggregate.getProjectAssignment().getProject().getPK();
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
