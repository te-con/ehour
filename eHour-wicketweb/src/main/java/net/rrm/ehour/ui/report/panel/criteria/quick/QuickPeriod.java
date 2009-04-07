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

package net.rrm.ehour.ui.report.panel.criteria.quick;

import java.io.Serializable;
import java.util.Date;


public abstract class QuickPeriod  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -245086949586026553L;
	private Date	periodStart;
	private Date	periodEnd;
	private	int		periodIndex;
	/**
	 * @return the periodStart
	 */
	public Date getPeriodStart()
	{
		return periodStart;
	}
	/**
	 * @return the periodEnd
	 */
	public Date getPeriodEnd()
	{
		return periodEnd;
	}
	/**
	 * @return the periodIndex
	 */
	public int getPeriodIndex()
	{
		return periodIndex;
	}
	/**
	 * @param periodStart the periodStart to set
	 */
	public void setPeriodStart(Date periodStart)
	{
		this.periodStart = periodStart;
	}
	/**
	 * @param periodEnd the periodEnd to set
	 */
	public void setPeriodEnd(Date periodEnd)
	{
		this.periodEnd = periodEnd;
	}
	/**
	 * @param periodIndex the periodIndex to set
	 */
	public void setPeriodIndex(int periodIndex)
	{
		this.periodIndex = periodIndex;
	}
}
