package net.rrm.ehour.ui.report.aggregate.value;

import net.rrm.ehour.report.reports.dto.AssignmentAggregateReportElement;

import java.io.Serializable;

/**
 * User: Thies
 * Date: Sep 11, 2007
 * Time: 7:48:31 PM
 * Copyright (C) 2005-2007 TE-CON, All Rights Reserved.
 * <p/>
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is
 * available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for
 * commercial use or open source, is subject to obtaining the prior express authorization of TE-CON.
 * <p/>
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 */
public abstract class ReportNodeFactory
{
    /**
     * Create report node for hierarchy level
     * @param hierarchyLevel
     * @return
     */
    public abstract ReportNode createReportNode(AssignmentAggregateReportElement aggregate, int hierarchyLevel);

    /**
     * Get the id of an aggregate
     * @param aggregate
     * @return
     */
    public abstract Serializable getAssignmentId(AssignmentAggregateReportElement aggregate);
}
