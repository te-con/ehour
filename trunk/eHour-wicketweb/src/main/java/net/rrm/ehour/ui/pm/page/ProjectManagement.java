/**
 * Created on Oct 6, 2007
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

package net.rrm.ehour.ui.pm.page;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.page.BasePage;
import net.rrm.ehour.ui.common.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import net.rrm.ehour.ui.report.panel.pm.PmReportPanel;
import net.rrm.ehour.ui.report.panel.user.criteria.UserReportCriteriaPanel;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Project management base station :)
 **/
@AuthorizeInstantiation("ROLE_PROJECTMANAGER")
public class ProjectManagement extends BasePage
{
	private static final long serialVersionUID = 898442184509251553L;

	@SpringBean
	private ProjectService	projectService;
	@SpringBean
	private AggregateReportService	aggregateReportService;
	private WebMarkupContainer	reportPanel;
	
	/**
	 * Default constructor 
	 * @param pageTitle
	 * @param model
	 */
	public ProjectManagement()
	{
		super(new ResourceModel("pmReport.title"), null);
		
		ReportCriteria reportCriteria = getReportCriteria();
		
		IModel	model = new CompoundPropertyModel(reportCriteria);
		setModel(model);
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp", "pmReport.help.header", "pmReport.help.body"));

		// add criteria
		add(new UserReportCriteriaPanel("sidePanel", model, false));
		
		reportPanel = new WebMarkupContainer("reportPanel");
		reportPanel.setOutputMarkupId(true);
		add(reportPanel);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.page.BasePage#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == ReportCriteriaAjaxEventType.CRITERIA_UPDATED && ajaxEvent.getTarget() != null)
		{
			WebMarkupContainer replacement = new PmReportPanel("reportPanel", getReportData());
			reportPanel.replaceWith(replacement);
			reportPanel = replacement;
			ajaxEvent.getTarget().addComponent(replacement);
		}
		
		return true;
	}

	/**
	 * Get report criteria
	 * @return
	 */
	private ReportCriteria getReportCriteria()
	{
		ReportCriteria reportCriteria = new ReportCriteria();
		
		User user = ((EhourWebSession)getSession()).getUser().getUser();
		
		List<Project> projects = projectService.getProjectManagerProjects(user);
		
		AvailableCriteria availCriteria = reportCriteria.getAvailableCriteria();
		availCriteria.setProjects(projects);
		
		return reportCriteria;
	}
	
	/**
	 * Get report data for  model
	 * @return
	 */
	private ProjectManagerReport getReportData()
	{
		ReportCriteria 	criteria = (ReportCriteria)(getModelObject());
		ProjectManagerReport reportData = null;
		DateRange	reportRange = criteria.getUserCriteria().getReportRange();
		
		if (criteria.getUserCriteria().isInfiniteStartDate())
		{
			reportRange.setDateStart(null);
		}

		if (criteria.getUserCriteria().isInfiniteEndDate())
		{
			reportRange.setDateEnd(null);
		}

		if (criteria.getUserCriteria().getProject() != null)
		{
			// only one can be there
			Project project = criteria.getUserCriteria().getProject();
			reportData = aggregateReportService.getProjectManagerDetailedReport(reportRange, project.getPK());
		}
		
		return reportData;
	}

	/**
	 * @param reportPanel the reportPanel to set
	 */
	public void setReportPanel(WebMarkupContainer reportPanel)
	{
		this.reportPanel = reportPanel;
	}	
}
