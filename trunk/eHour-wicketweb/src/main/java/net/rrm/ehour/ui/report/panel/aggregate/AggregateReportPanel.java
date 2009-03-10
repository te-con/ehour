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

package net.rrm.ehour.ui.report.panel.aggregate;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.report.ReportDrawType;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.panel.AbstractReportPanel;
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;

/**
 * Full report panel containing report data and the charts 
 **/

public abstract class AggregateReportPanel extends AbstractReportPanel
{
	private static final long serialVersionUID = 2173644826934093029L;

	/**
	 * 
	 * @param id
	 */
	public AggregateReportPanel(String id, TreeReport<AssignmentAggregateReportElement> reportData,
									ReportData<AssignmentAggregateReportElement> data, 
									ReportConfig reportConfig, String excelResourceName,
									ReportDrawType drawType)
	{
		super(id, 460);

		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("reportFrame", getReportWidth());
		add(greyBorder);

		greyBorder.add(new TreeReportDataPanel("reportTable", reportData, reportConfig, excelResourceName, getReportWidth() - 50));
		
		if (drawType == ReportDrawType.IMAGE)
		{
			addImageCharts(data, greyBorder);
		}
		else
		{
			addOpenFlashCharts(data, greyBorder);
		}
	}
	
	private void addOpenFlashCharts(ReportData<AssignmentAggregateReportElement> data, WebMarkupContainer parent)
	{
		Fragment fragment = new Fragment("charts", "flash", this);
		addFlashCharts(data, fragment);
		parent.add(fragment);
	}
	
	private void addImageCharts(ReportData<AssignmentAggregateReportElement> data, WebMarkupContainer parent)
	{
		Fragment fragment = new Fragment("charts", "image", this);
		addCharts(data, fragment);
		parent.add(fragment);
	}
	
	/**
	 * Add image charts
	 * @param reportCriteria
	 * @return
	 */
	protected void addCharts(ReportData<AssignmentAggregateReportElement> data, WebMarkupContainer parent)
	{
		
	}
	
	/**
	 * Add image charts. Should add 2 components to the parent with id hoursChart and turnoverChart
	 * @param reportCriteria
	 * @return
	 */
	protected void addFlashCharts(ReportData<AssignmentAggregateReportElement> data, WebMarkupContainer parent)
	{
		
	}	
}
