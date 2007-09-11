package net.rrm.ehour.ui.report.aggregate;

import net.rrm.ehour.ui.report.aggregate.value.ReportNode;

import java.io.Serializable;
import java.util.List;

/**
 * User: Thies
 * Date: Sep 11, 2007
 * Time: 9:28:30 PM
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
public interface AggregateReport extends Serializable
{
    /**
     * Get report nodes
     * @return
     */
    public List<ReportNode> getNodes();
}
