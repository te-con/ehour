/**
 * Created on Sep 27, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.panel.report;

import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.report.aggregate.AggregateReport;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Full report panel containing report data and the charts 
 **/

public class ReportPanel extends Panel
{
	private static final long serialVersionUID = -1562027888605553288L;

	/**
	 * 
	 * @param id
	 */
	public ReportPanel(String id, AggregateReport reportData)
	{
		super(id);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("reportFrame");

		greyBorder.add(new AggregateReportDataPanel("reportTable", reportData, ReportType.AGGREGATE_CUSTOMER));
		
		add(greyBorder);
	}
}
