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

package net.rrm.ehour.ui.report.node;

import java.io.Serializable;

import net.rrm.ehour.report.reports.element.ReportElement;

/**
 * Factory for report nodes
 * @author Thies
 *
 */
public abstract class ReportNodeFactory
{
    /**
     * Create report node for hierarchy level
     * @param hierarchyLevel
     * @return
     */
    public abstract ReportNode createReportNode(ReportElement aggregate, int hierarchyLevel);

    /**
     * Get the id of a report element
     * @param aggregate
     * @return
     */
    public abstract Serializable getElementId(ReportElement aggregate);
}
