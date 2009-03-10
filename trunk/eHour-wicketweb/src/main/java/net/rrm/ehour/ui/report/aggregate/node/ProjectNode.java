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

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.node.ReportNode;

/**
 * Project node displaying project full name 
 */
public class ProjectNode extends ReportNode
{
	private static final long serialVersionUID = -8068372785700592324L;

	public ProjectNode(AssignmentAggregateReportElement aggregate, int hierarchyLevel)
    {
        this.id = aggregate.getProjectAssignment().getProject().getPK();
        this.columnValues = new String[]{aggregate.getProjectAssignment().getProject().getName(),
                                         aggregate.getProjectAssignment().getProject().getProjectCode()};
        this.hierarchyLevel = hierarchyLevel;
    }

    @Override
    protected Serializable getElementId(ReportElement element)
    {
    	AssignmentAggregateReportElement aggregate = (AssignmentAggregateReportElement)element;
        return aggregate.getProjectAssignment().getProject().getPK();
    }
}