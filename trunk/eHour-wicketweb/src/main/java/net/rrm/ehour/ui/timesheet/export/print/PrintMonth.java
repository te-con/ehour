/**
 * Created on Sep 30, 2007
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

package net.rrm.ehour.ui.timesheet.export.print;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.ui.report.trend.PrintReport;
import net.rrm.ehour.ui.timesheet.export.ExportCriteriaParameter;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Print month page
 **/
@AuthorizeInstantiation("ROLE_CONSULTANT")
public class PrintMonth extends WebPage
{
	private static final long serialVersionUID = 1891959724639181159L;
	
	@SpringBean
	private DetailedReportService	detailedReportService;
	private EhourConfig				config;
	
	/**
	 * 
	 */
	public PrintMonth(ReportCriteria reportCriteria)
	{
		super();

		EhourWebSession session = (EhourWebSession)getSession();
		config = session.getEhourConfig();
		
		try
		{
			DateRange dateRange = reportCriteria.getUserCriteria().getReportRange();
			
			PrintReport printReport = initReport(reportCriteria);

			IModel printTitle = new StringResourceModel("printMonth.printHeader",
					this,
					null,
					new Object[]{session.getUser().getUser().getFullName(),
								 new DateModel(dateRange.getDateStart() , config, DateModel.DATESTYLE_MONTHONLY)});
			
			Label pageTitle = new Label("pageTitle", printTitle);
			add(pageTitle);
			
			Label reportHeader = new Label("printHeader", printTitle);
			add(reportHeader);

			List<Date> dates = DateUtil.createDateSequence(dateRange, config);
			
			addDateLabels(dates);
			addProjects(printReport, dates);
			addGrandTotal(printReport, dates);
			
			add(createSignOff(reportCriteria, dates));
			
			add(new Label("printedOn", new StringResourceModel("printMonth.printedOn",
					this,
					null,
					new Object[]{new DateModel(new GregorianCalendar() , config)})));
			
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block. Handle better
			e.printStackTrace();
		}
	}

	private WebMarkupContainer createSignOff(ReportCriteria reportCriteria, List<Date> days)
	{
		WebMarkupContainer signOffContainer = new WebMarkupContainer("signOff");
		signOffContainer.setVisible(isSignOffEnabled(reportCriteria));
		
		signOffContainer.add(new Label("userFullName", ((EhourWebSession)getSession()).getUser().getUser().getFullName()));
		
		WebMarkupContainer colspanner = new WebMarkupContainer("colspanner");
		// got 16 cells left, 16 cells right
		colspanner.add(new SimpleAttributeModifier("colspan", Integer.toString(1 + days.size() -15 -16 )));
		signOffContainer.add(colspanner);
		
		return signOffContainer;
	}
	
	private boolean isSignOffEnabled(ReportCriteria reportCriteria)
	{
		Boolean signOff = (Boolean)reportCriteria.getUserCriteria().getCustomParameters().get(ExportCriteriaParameter.INCL_SIGN_OFF.name());

		return (signOff == null) ? false : signOff.booleanValue();
	}
	
	/**
	 * Add grand total
	 * @param report
	 * @param days
	 */
	private void addGrandTotal(PrintReport report, final List<Date> days)
	{
		Label label = new Label("grandTotal", new FloatModel(report.getGrandTotalHours(), config));
		label.add(new SimpleAttributeModifier("colspan", Integer.toString(days.size() + 2)));
		add(label);
	}
	
	/**
	 * Add projects
	 * @param report
	 */
	private void addProjects(final PrintReport report, final List<Date> days)
	{
		ListView reports = new ListView("projects", new ArrayList<ProjectAssignment>(report.getValues().keySet()))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				addProjectRow(item, days, report);
			}
		};
		
		add(reports);
	}
	
	/**
	 * Add rows per project
	 * @param item
	 * @param days
	 * @param report
	 */
	private void addProjectRow(ListItem item, List<Date> days, PrintReport report)
	{
		ProjectAssignment assignment = (ProjectAssignment)item.getModelObject();

		item.add(new Label("project", assignment.getProject().getFullName()));
		
		Label role = new Label("role", "( " + assignment.getRole() + " )");
		role.setVisible(assignment.getRole() != null && !assignment.getRole().trim().equals(""));
		item.add(role);
		
		Map<Date, FlatReportElement> assignmentMap = report.getValues().get(assignment);
		
		RepeatingView dateLabels = new RepeatingView("days");
		
		float total = 0;
		
		for (Date day : days)
		{
			Label label = null;
			
			if (assignmentMap.containsKey(day))
			{
				FlatReportElement aggregate = assignmentMap.get(day);
				
				if (aggregate.getTotalHours() != null && aggregate.getTotalHours().floatValue() > 0)
				{
					label = new Label(dateLabels.newChildId(), new FloatModel(aggregate.getTotalHours(), config));
					
					total += aggregate.getTotalHours().floatValue();
				}
			}
			
			if (label == null)
			{
				label = HtmlUtil.getNbspLabel(dateLabels.newChildId());
			}
			
			dateLabels.add(label);
		}		
		
		item.add(dateLabels);
		
		item.add(new Label("total", new FloatModel(total, config)));
	}
	
	
	/**
	 * Add date labels
	 * @param printRange
	 */
	private void addDateLabels(List<Date> dates)
	{
		RepeatingView dateLabels = new RepeatingView("days");
		
		for (Date date : dates)
		{
			dateLabels.add(new Label(dateLabels.newChildId(), new DateModel(date, config, DateModel.DATESTYLE_DAYONLY)));
		}
		
		add(dateLabels);
	}
	
	/**
	 * 
	 * @param assignmentIds
	 * @param printRange
	 * @return
	 * @throws ParseException
	 */
	private PrintReport initReport(ReportCriteria criteria) throws ParseException
	{
		ReportData<FlatReportElement> detailedReportData = detailedReportService.getDetailedReportData(criteria);
		
		PrintReport printReport = new PrintReport();
		printReport.initialize(detailedReportData.getReportElements());
		
		return printReport;
	}
}
