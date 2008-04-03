/**
 * Created on Apr 7, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.project.status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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
		AFTER_DEADLINE;
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
