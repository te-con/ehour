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
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.report.AbstractAggregateReport;
import net.rrm.ehour.ui.report.aggregate.node.CustomerNode;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.report.node.ReportNodeFactory;

import java.io.Serializable;

public class UserAggregateReport extends AbstractAggregateReport
{
	private static final long serialVersionUID = 2883703894793044411L;

	/**
	 * 
	 * @param reportData
	 */
	public UserAggregateReport(ReportCriteria reportCriteria)
	{
		super(reportCriteria, ReportConfig.AGGREGATE_EMPLOYEE);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.report.aggregate.AggregateReport#getReportNodeFactory()
	 */
	@Override
	public ReportNodeFactory<AssignmentAggregateReportElement> getReportNodeFactory()
	{
    	return new ReportNodeFactory<AssignmentAggregateReportElement>()
	    {
	        @Override
	        public ReportNode createReportNode(AssignmentAggregateReportElement aggregate, int hierarchyLevel)
	        {
	            switch (hierarchyLevel)
	            {
	                case 0:
	                    return new UserNode(aggregate, hierarchyLevel);
	                case 1:
	                    return new CustomerNode(aggregate, hierarchyLevel);
	                case 2:
	                    return new ProjectEndNode(aggregate, hierarchyLevel);
	            }
	
	            throw new RuntimeException("Hierarchy level too deep");
	        }
	
	        /**
	         * Only needed for the root node, customer
	         * @param aggregate
	         * @return
	         */
	
	        public Serializable getElementId(AssignmentAggregateReportElement aggregate)
	        {
	            return aggregate.getProjectAssignment().getUser().getPK();
	        }
	    };
	}

	private final class UserNode extends ReportNode
	{
		private static final long serialVersionUID = 8534482324216994500L;

		private UserNode(AssignmentAggregateReportElement aggregate, int hierarchyLevel)
		{
	        this.id = aggregate.getProjectAssignment().getUser().getPK();
	        this.columnValues = new Serializable[]{aggregate.getProjectAssignment().getUser().getFullName()};
			
			this.hierarchyLevel = hierarchyLevel;
		}

		@Override
		protected Serializable getElementId(ReportElement element)
		{
			AssignmentAggregateReportElement aggregate = (AssignmentAggregateReportElement)element;
			
			return aggregate.getProjectAssignment().getUser().getPK();		
		}
	}
	
	private final class ProjectEndNode extends ReportNode
	{
		private static final long serialVersionUID = 1L;
		
		private Number   hours;
	    private Number   turnOver;		
		
	    private ProjectEndNode(AssignmentAggregateReportElement aggregate, int hierarchyLevel)
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
	        
	        this.hierarchyLevel = hierarchyLevel;

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
