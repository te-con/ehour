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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.node.ReportNode;

/**
 * Day date node 
 **/

public class FlatDateNode extends ReportNode
{
	private static final long serialVersionUID = 7854152602780377915L;
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 
	 * @param element
	 * @param hierarchyLevel
	 */
	public FlatDateNode(FlatReportElement element, int hierarchyLevel, Locale locale)
    {
		this.id = formatter.format(element.getDayDate());
		this.columnValues = new Date[]{element.getDayDate()};
		this.hierarchyLevel = hierarchyLevel;
    }

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.node.ReportNode#getElementId(net.rrm.ehour.report.reports.element.ReportElement)
	 */
	@Override
	protected Serializable getElementId(ReportElement element)
	{
		return formatter.format(((FlatReportElement)element).getDayDate());
	}
}
