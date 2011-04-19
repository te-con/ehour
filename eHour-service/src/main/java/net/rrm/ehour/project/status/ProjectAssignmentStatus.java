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

package net.rrm.ehour.project.status;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectAssignment status
 *  
 * A flex assignment has 3 statusses: 
 *  - booked hours before allotted hours mark (IN_ALLOTTED_PHASE)
 * 	- over  the allotted hours mark but before the overrun mark (IN_OVERRUN_PHASE)
 *  - over the overrun mark, no more hours can be booked and mail should be sent (OVER_OVERRUN_PHASE)
 *  
 * A fixed assignment has 2 statusses:
 *  - booked hours before allotted hours mark (IN_ALLOTTED_PHASE)
 *  - over the alloted hours mark, no more hours can be booked and mail should be sent (OVER_ALLOTTED_PHASE)
 *
 * Additionaly an assignment has either a before start, running, after deadline status
 **/

public class ProjectAssignmentStatus implements Serializable
{
	private static final long serialVersionUID = 6826582447867247739L;

	public enum Status
	{
		IN_OVERRUN,
		IN_ALLOTTED,
		OVER_ALLOTTED,
		OVER_OVERRUN,
		BEFORE_START,
		RUNNING,
		AFTER_DEADLINE
    }
	
	private AssignmentAggregateReportElement	aggregate;
	private List<Status> statusses;
	private boolean valid;
	
	public ProjectAssignmentStatus()
	{
		valid = true;
	}
	
	/**
	 * @return the valid
	 */
	public boolean isValid()
	{
		return valid;
	}

	/**
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid)
	{
		this.valid = valid;
	}

	/**
	 * Can assignment alive?
	 * @return
	 */
	public boolean isAssignmentBookable()
	{
		boolean isBookable = true;
		
		for (Status status : statusses)
		{
			isBookable &= (status == Status.RUNNING || 
							status == Status.IN_OVERRUN || 
							status == Status.IN_ALLOTTED);
		}
		
		return isBookable;
	}
	
	/**
	 * Add status
	 * @param status
	 */
	public void addStatus(Status status)
	{
		if (statusses == null)
		{
			statusses  = new ArrayList<Status>();
		}
		
		statusses.add(status);
	}
	

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof ProjectAssignmentStatus))
		{
			return false;
		}
		ProjectAssignmentStatus pas = (ProjectAssignmentStatus) object;
		
		return new EqualsBuilder()
			.append(this.getStatusses(), pas.getStatusses())
			.isEquals();
	}	
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder(1202909165, -339864927)
			.appendSuper(super.hashCode())
			.append(this.getStatusses())
			.toHashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new ToStringBuilder(this)
				.append("statusses", this.getStatusses())
				.append("aggregate", this.getAggregate())
				.toString();
	}
	
	
	/**
	 * @return the aggregate
	 */
	public AssignmentAggregateReportElement getAggregate()
	{
		return aggregate;
	}
	/**
	 * @param aggregate the aggregate to set
	 */
	public void setAggregate(AssignmentAggregateReportElement aggregate)
	{
		this.aggregate = aggregate;
	}

	/**
	 * @return the statusses
	 */
	public List<Status> getStatusses()
	{
		return statusses;
	}

	/**
	 * @param statusses the statusses to set
	 */
	public void setStatusses(List<Status> statusses)
	{
		this.statusses = statusses;
	}
}
