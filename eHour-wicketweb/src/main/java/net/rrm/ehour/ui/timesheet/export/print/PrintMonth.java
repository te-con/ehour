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

package net.rrm.ehour.ui.timesheet.export.print;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.ui.common.model.DateModel;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

/**
 * Print month page
 **/
@AuthorizeInstantiation("ROLE_CONSULTANT")
public class PrintMonth extends WebPage
{
	private static final long serialVersionUID = 1891959724639181159L;
	
	private EhourConfig				config;
	
	/**
	 * 
	 */
	public PrintMonth(ReportCriteria reportCriteria)
	{
		super();

		EhourWebSession session = (EhourWebSession)getSession();
		config = session.getEhourConfig();
		
		DateRange dateRange = reportCriteria.getReportRange();
		
		PrintReport printReport = new PrintReport(reportCriteria);

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
		Label label = new Label("grandTotal", new Model<Float>(report.getGrandTotalHours()));
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
					label = new Label(dateLabels.newChildId(), new Model<Float>(aggregate.getTotalHours().floatValue()));
					
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
		
		item.add(new Label("total", new Model<Float>(total)));
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
}
