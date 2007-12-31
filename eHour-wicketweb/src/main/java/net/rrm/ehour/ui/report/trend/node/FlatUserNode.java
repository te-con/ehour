/**
 * Created on Dec 31, 2007
 * Author: Thies
 *
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

package net.rrm.ehour.ui.report.trend.node;

import java.io.Serializable;

import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.node.ReportNode;

/**
 * User node
 **/

public class FlatUserNode extends ReportNode
{
	private static final long serialVersionUID = 7854152602780377915L;
	
	/**
	 * 
	 * @param element
	 * @param hierarchyLevel
	 */
	public FlatUserNode(FlatReportElement element, int hierarchyLevel)
    {
		this.id = element.getUserId();
		this.columnValues = new String[]{element.getUserLastName()};
		this.hierarchyLevel = hierarchyLevel;
    }

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.node.ReportNode#getElementId(net.rrm.ehour.report.reports.element.ReportElement)
	 */
	@Override
	protected Serializable getElementId(ReportElement element)
	{
		return ((FlatReportElement)element).getUserId();
	}

}
