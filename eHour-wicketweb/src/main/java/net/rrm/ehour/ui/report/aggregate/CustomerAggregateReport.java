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
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.report.AbstractAggregateReport;
import net.rrm.ehour.ui.report.aggregate.node.CustomerNode;
import net.rrm.ehour.ui.report.aggregate.node.ProjectNode;
import net.rrm.ehour.ui.report.aggregate.node.UserEndNode;
import net.rrm.ehour.ui.report.node.ReportNode;
import net.rrm.ehour.ui.report.node.ReportNodeFactory;

import java.io.Serializable;

public class CustomerAggregateReport extends AbstractAggregateReport
{
	private static final long serialVersionUID = -3221674649410450972L;

	public CustomerAggregateReport(ReportCriteria reportCriteria)
	{
		super(reportCriteria, ReportConfig.AGGREGATE_CUSTOMER);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.report.TreeReport#getReportNodeFactory()
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
	                	return (aggregate != null) ? new CustomerNode(aggregate, hierarchyLevel) : null;
	                case 1:
	                    return new ProjectNode(aggregate, hierarchyLevel);
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
	        public Serializable getElementId(AssignmentAggregateReportElement aggregate)
	        {
	            return aggregate.getProjectAssignment().getProject().getCustomer().getPK();
	        }
	    };
    }
}
