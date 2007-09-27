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
 * End node displaying user's full name, hours and turnover 
 **/

public class UserEndNode extends ReportNode
{
	private static final long serialVersionUID = 3861923371702158088L;
	private Number   hours;
    private Number   turnOver;

    public UserEndNode(ProjectAssignmentAggregate aggregate)
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