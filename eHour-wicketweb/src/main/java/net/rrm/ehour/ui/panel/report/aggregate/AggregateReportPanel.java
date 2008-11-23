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

package net.rrm.ehour.ui.panel.report.aggregate;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.panel.report.AbstractReportPanel;
import net.rrm.ehour.ui.panel.report.ReportConfig;
import net.rrm.ehour.ui.panel.report.TreeReportDataPanel;
import net.rrm.ehour.ui.report.TreeReport;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Full report panel containing report data and the charts 
 **/

public abstract class AggregateReportPanel extends AbstractReportPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2173644826934093029L;

	/**
	 * 
	 * @param id
	 */
	public AggregateReportPanel(String id, TreeReport<AssignmentAggregateReportElement> reportData,
									ReportData<AssignmentAggregateReportElement> data, ReportConfig reportConfig, String excelResourceName)
	{
		super(id, 460);

		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("reportFrame", getReportWidth());
		add(greyBorder);

		greyBorder.add(new TreeReportDataPanel("reportTable", reportData, reportConfig, excelResourceName, getReportWidth() - 50));
		
		addCharts(data, greyBorder);
	}
	
	/**
	 * Add charts
	 * @param reportCriteria
	 * @return
	 */
	protected abstract void addCharts(ReportData<AssignmentAggregateReportElement> data, WebMarkupContainer parent);
}
