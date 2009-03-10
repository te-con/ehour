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
 * Entry end node
 **/

public class FlatEntryEndNode extends ReportNode
{
	private static final long serialVersionUID = 7854152602780377915L;
	private Number hours;
	private Number turnOver;
	
	/**
	 * 
	 * @param element
	 * @param hierarchyLevel
	 */
	public FlatEntryEndNode(FlatReportElement element, int hierarchyLevel)
    {
        hours = element.getTotalHours();
        turnOver = element.getTotalTurnOver();
        
		this.id = element.getDisplayOrder();
		this.columnValues = new Serializable[]{element.getComment(), 
												element.getTotalHours(), element.getTotalTurnOver()};
		this.hierarchyLevel = hierarchyLevel;
    }

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.node.ReportNode#getElementId(net.rrm.ehour.report.reports.element.ReportElement)
	 */
	@Override
	protected Serializable getElementId(ReportElement element)
	{
		return ((FlatReportElement)element).getDisplayOrder();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.node.ReportNode#getHours()
	 */
    @Override
    public Number getHours()
    {
        return hours; 
    }

    /*
     * (non-Javadoc)
     * @see net.rrm.ehour.ui.report.node.ReportNode#getTurnover()
     */
    @Override
    public Number getTurnover()
    {
        return turnOver;
    }	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.node.ReportNode#isLastNode()
	 */
    @Override
    protected boolean isLastNode()
    {
        return true;
    }	
}
