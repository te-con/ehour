/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
