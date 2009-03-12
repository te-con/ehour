/**
 * Created on 22-feb-2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.reports;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.element.ReportElement;

/**
 * Data holder for aggregate reports
 **/

public class ReportData implements Serializable
{
	private static final long serialVersionUID = -6344570520998830487L;
	
	private List<? extends ReportElement>		reportElements;
	private DateRange reportRange;
	
	public ReportData(List<? extends ReportElement> reportElements, DateRange reportRange)
	{
		this.reportElements = reportElements;
		this.reportRange = reportRange;
	}
	
	/**
	 * @return the reportRange
	 */
	public DateRange getReportRange()
	{
		return reportRange;
	}
	
	/**
	 * @return the reportElements
	 */
	public List<? extends ReportElement> getReportElements()
	{
		return reportElements;
	}
}
