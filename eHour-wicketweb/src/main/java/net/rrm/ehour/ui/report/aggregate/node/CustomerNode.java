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

package net.rrm.ehour.ui.report.aggregate.node;

import java.io.Serializable;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;

/**
 * Customer node in report
 **/

public class CustomerNode extends ReportNode
{
	private static final long serialVersionUID = -356525734449023397L;

	public CustomerNode(ProjectAssignmentAggregate aggregate, int hierarchyLevel)
    {
		if (aggregate.getProjectAssignment() != null)
		{
	        this.id = aggregate.getProjectAssignment().getProject().getCustomer().getPK();
	        this.columnValues = new String[]{aggregate.getProjectAssignment().getProject().getCustomer().getFullName()};
	        this.hierarchyLevel = hierarchyLevel;
		}
    }

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.aggregate.value.ReportNode#getAggregateId(net.rrm.ehour.report.reports.ProjectAssignmentAggregate)
	 */
	@Override
    protected Serializable getAggregateId(ProjectAssignmentAggregate aggregate)
    {
		if (aggregate.getProjectAssignment() != null)
		{
			return aggregate.getProjectAssignment().getProject().getCustomer().getPK();
		}
		else
		{
			return new Integer(1);
		}
    }
}