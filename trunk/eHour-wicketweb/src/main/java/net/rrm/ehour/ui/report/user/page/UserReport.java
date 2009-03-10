/**
 * Created on Jul 9, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.report.user.page;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.page.AbstractReportPage;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import net.rrm.ehour.ui.report.panel.user.UserReportPanel;
import net.rrm.ehour.ui.report.panel.user.criteria.UserReportCriteriaPanel;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Reporting for user
 **/

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class UserReport extends AbstractReportPage
{
	private static final long serialVersionUID = -8867366237264687482L;

	private WebMarkupContainer		reportDataPanel;
	@SpringBean
	private AggregateReportService		aggregateReportService;

	/**
	 * 
	 */
	public UserReport()
	{
		super(new ResourceModel("userreport.title"));
		
		ReportCriteria reportCriteria = getReportCriteria(true);
		IModel	model = new CompoundPropertyModel(reportCriteria);
		setModel(model);

		// add criteria
		add(new UserReportCriteriaPanel("sidePanel", model));

		// add data
		reportDataPanel = getReport();
		add(reportDataPanel);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.page.BasePage#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == ReportCriteriaAjaxEventType.CRITERIA_UPDATED)
		{
			WebMarkupContainer	replacement = getReport();
			reportDataPanel.replaceWith(replacement);
			reportDataPanel = replacement;
			ajaxEvent.getTarget().addComponent(replacement);
		}
		
		return false;
	}
	
	/**
	 * Get report
	 * @return
	 */
	private WebMarkupContainer getReport()
	{
		ReportCriteria criteria = (ReportCriteria)getModel().getObject();
		
		// add data
		ReportData<AssignmentAggregateReportElement> reportData = getAggregateReportData(criteria);
		CustomerAggregateReport	customerAggregateReport = new CustomerAggregateReport(reportData);
		((EhourWebSession)(getSession())).getReportCache().addReportToCache(customerAggregateReport, reportData);
		
		UserReportPanel panel = new UserReportPanel("userReportPanel", customerAggregateReport, reportData, true);
		panel.setOutputMarkupId(true);
		return panel;
	}
	
	/**
	 * Get aggregated report data
	 * @param reportCriteria
	 * @return
	 */
	private ReportData<AssignmentAggregateReportElement> getAggregateReportData(ReportCriteria reportCriteria)
	{
		logger.debug("Getting aggregated report data");
		ReportData<AssignmentAggregateReportElement> data = aggregateReportService.getAggregateReportData(reportCriteria);
		
		return data;
	}	
}
