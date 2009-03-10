/**
 * Created on Dec 13, 2008
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

package net.rrm.ehour.ui.common.report;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.ui.report.Report;

/**
 * Report with data stored in a matrix
 **/

public abstract class RangedReport extends Report
{
	private static final long serialVersionUID = 1L;

	private	DateRange				reportRange;

	
	/**
	 * 
	 * @return
	 */
	public abstract List<Serializable[]> getReportMatrix();


	/**
	 * @return the reportRange
	 */
	public DateRange getReportRange()
	{
		return reportRange;
	}


	/**
	 * @param reportRange the reportRange to set
	 */
	protected void setReportRange(DateRange reportRange)
	{
		this.reportRange = reportRange;
	}
}
