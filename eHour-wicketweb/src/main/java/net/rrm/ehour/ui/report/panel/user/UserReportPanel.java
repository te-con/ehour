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

package net.rrm.ehour.ui.report.panel.user;

import java.util.Arrays;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.service.report.reports.ReportData;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.ui.report.TreeReportData;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.chart.AggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.aggregate.AggregateChartImage;
import net.rrm.ehour.ui.report.chart.aggregate.CustomerHoursAggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.aggregate.CustomerTurnoverAggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.aggregate.ProjectHoursAggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.aggregate.ProjectTurnoverAggregateChartDataConverter;
import net.rrm.ehour.ui.report.panel.AbstractReportPanel;
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel;
import net.rrm.ehour.ui.report.user.page.UserReportPrint;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

/**
 * Report table
 **/

public class UserReportPanel extends AbstractReportPanel
{
	public enum Option
	{
		INCLUDE_LINKS;
	}
	
	private static final String PROJECT_TURNOVER_CHART_ID = "projectTurnoverChart";
	private static final String CUSTOMER_TURNOVER_CHART_ID = "customerTurnoverChart";
	private static final long serialVersionUID = -2660092982421858132L;
	
	/**
	 * 
	 * @param id
	 * @param reportData
	 */
	public UserReportPanel(String id, CustomerAggregateReport aggregateReport, Option... options)
	{
		super(id, WebGeo.NOT_DEFINED, WebGeo.W_CONTENT_MEDIUM);
		add(getReportPanel(aggregateReport, options));
	}
	
	/**
	 * Get report panel
	 * @param customerAggregateReport
	 * @return
	 */
	private WebMarkupContainer getReportPanel(CustomerAggregateReport customerAggregateReport, Option... options)
	{
		final EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		
		ResourceLink<String> 	excelLink = null;
		Link<String>			printLink = null;
		List<Option> optionList = Arrays.asList(options);
		
		if (optionList.contains(Option.INCLUDE_LINKS))
		{
			final String reportId = customerAggregateReport.getCacheId();
			
			ResourceReference excelResource = new ResourceReference(UserReportExcel.getId());
			ValueMap params = new ValueMap();
			params.add("reportId", reportId);
			excelLink = new ResourceLink<String>("excelLink", excelResource, params);
			
			printLink = new Link<String>("printLink")
			{
				private static final long serialVersionUID = 7739711270923594216L;

				@Override
				public void onClick()
				{
					setResponsePage(new UserReportPrint(reportId));
				}
			};
			
			printLink.add(new SimpleAttributeModifier("target", "_print"));
			
		}

		// Report model
		StringResourceModel reportTitle = new StringResourceModel("userReport.title", 
																this, null, 
																new Object[]{new DateModel(customerAggregateReport.getReportRange().getDateStart(), config),
																			 new DateModel(customerAggregateReport.getReportRange().getDateEnd(), config)});
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("reportFrame", reportTitle, true, printLink, excelLink, WebGeo.W_CONTENT_MEDIUM);

		greyBorder.add(new TreeReportDataPanel("reportTable", customerAggregateReport, ReportConfig.AGGREGATE_CUSTOMER_SINGLE_USER, null, getReportWidth().getValue() - 30));
		
		Fragment frag = new Fragment("charts", "image", this);
		greyBorder.add(frag);
		
		addImageCharts(customerAggregateReport.getReportData(), frag);
		
		return greyBorder;
	}
	
	/**
	 * Add jfree charts
	 * @param reportCriteria
	 * @return
	 */
	private void addImageCharts(ReportData data, WebMarkupContainer parent)
	{
		final EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		
		ReportData rawData = ((TreeReportData)data).getRawReportData();
		
		Model<ReportData> dataModel = new Model<ReportData>(rawData);
		
		// hours per customer
		parent.add(createCustomerHoursChart(dataModel));

		// turnover per customer
		parent.add(config.isShowTurnover() ? createCustomerTurnOverChart(dataModel) : createInvisibleImage(CUSTOMER_TURNOVER_CHART_ID));

		// hours per project
		parent.add(createProjectHoursChart(dataModel));

		// turnover per project
		parent.add(config.isShowTurnover() ? createProjectTurnOverChart(dataModel) : createInvisibleImage(PROJECT_TURNOVER_CHART_ID));
	}

	private Image createInvisibleImage(String id)
	{
		Image img = new Image(id);
		img.setVisible(false);
		return img;
	}

	private Image createProjectHoursChart(Model<ReportData> dataModel)
	{
		AggregateChartDataConverter hourConverter = new ProjectHoursAggregateChartDataConverter();
		return new AggregateChartImage("projectHoursChart", dataModel, getChartWidth().getValue(), getChartHeight().getValue(), hourConverter);
	}	

	private Image createProjectTurnOverChart(Model<ReportData> dataModel)
	{
		AggregateChartDataConverter turnoverConverter = new ProjectTurnoverAggregateChartDataConverter();
		return new AggregateChartImage(PROJECT_TURNOVER_CHART_ID, dataModel, getChartWidth().getValue(), getChartHeight().getValue(), turnoverConverter);
	}

	
	private Image createCustomerTurnOverChart(Model<ReportData> dataModel)
	{
		AggregateChartDataConverter turnoverConverter = new CustomerTurnoverAggregateChartDataConverter();
		return new AggregateChartImage(CUSTOMER_TURNOVER_CHART_ID, dataModel, getChartWidth().getValue(), getChartHeight().getValue(), turnoverConverter);
	}

	private Image createCustomerHoursChart(Model<ReportData> dataModel)
	{
		AggregateChartDataConverter hourConverter = new CustomerHoursAggregateChartDataConverter();
		return new AggregateChartImage("customerHoursChart", dataModel, getChartWidth().getValue(), getChartHeight().getValue(), hourConverter);
	}	
}
