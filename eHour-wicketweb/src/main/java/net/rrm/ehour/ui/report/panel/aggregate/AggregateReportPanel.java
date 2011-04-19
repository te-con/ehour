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

package net.rrm.ehour.ui.report.panel.aggregate;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.common.util.WebGeo;
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

	public AggregateReportPanel(String id, TreeReport report,
									ReportConfig reportConfig, String excelResourceName)
	{
		super(id, WebGeo.W_CHART_MEDIUM);

		setOutputMarkupId(true);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("reportFrame", getReportWidth());
		add(greyBorder);

		greyBorder.add(new TreeReportDataPanel("reportTable", report, reportConfig, excelResourceName, getReportWidth().getValue() - 50));
		
		ReportData reportData = report.getReportData();
		
		addImageCharts(reportData, greyBorder);
	}
	
	private void addImageCharts(ReportData data, WebMarkupContainer parent)
	{
		Fragment fragment = new Fragment("charts", "image", this);
		addCharts("hoursChart", "turnoverChart", data, fragment);
		parent.add(fragment);
	}
	
	/**
	 * Add image charts
	 * @param reportCriteria
	 * @return
	 */
	protected abstract void addCharts(String hourId, String turnoverId, ReportData data, WebMarkupContainer parent);
}
