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
